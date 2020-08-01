package net;

import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.AbstractOrpheusServerNonChatProtocol;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.JsonException;
import net.messages.ServerMessage;
import net.protocols.ChatProtocol;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
import users.RemoteUser;
import util.SafeList;

/**
 * OrpheusServer is a somewhat deceptive title, as this is
 * used to establish peer-to-peer connections between players.
 * 
 * this class handles all of the interactions between computers, including
 * <ul>
 * <li>Chat</li>
 * <li>Joining a pre-match waiting room</li>
 * <li>Joining a World</li>
 * </ul>
 * 
 * Information is sent between OrpheusServers as ServerMessages,
 * encoded into JSON format, then converted into a String.
 * Upon receiving input into its socket, the server will attempt to de-serialize it.
 * If the de-serialization is successful, it takes the type of that message,
 * and calls the corresponding Consumers in the 'receivers' HashMap.
 * 
 * 
 * 
 * Still have some cleanup I want to do: Lotta nested stuff
 * 
 * @see net.protocols.AbstractOrpheusServerNonChatProtocol
 * 
 * @author Matt Crow
 */
public class OrpheusServer {
    private volatile boolean isStarted;
    private final ServerSocket server;
    private final HashSet<InetAddress> validIpAddresses;
    
    /*
    The users connected to this server, where the key is their
    IP address.
    */
    private final HashMap<InetAddress, Connection> connections;
    private Thread connListener; //the thread that listens for attemts to connect to this server
    private volatile boolean listenForConn; //whether or not the connListener thread is active
    
    private final SafeList<ServerMessagePacket> cachedMessages; //messages received before the receiver could be
    
    private volatile AbstractOrpheusServerNonChatProtocol currentProtocol;
    private volatile ChatProtocol currentChatProtocol;
    
    public static final int PORT = 5000;
    private static OrpheusServer instance = null;
    
    /**
     * Creates an OrpheusServer.
     * Note that this does not actually start the server,
     * you need to call start() for that.
     */
    private OrpheusServer() throws IOException{
        if(instance != null){
            throw new ExceptionInInitializerError("OrpheusServer is a singleton class: Use OrpheusServer.getInstance()");
        }
        server = new ServerSocket(PORT);
        validIpAddresses = getValidInetAddresses();
        
        connections = new HashMap<>();
        cachedMessages = new SafeList<>();
        listenForConn = false;   
        
        currentProtocol = null;
        currentChatProtocol = null;
        connListener = null;
        
        isStarted = false;
    }
    
    /**
     * 
     * @return
     * @throws SocketException if this cannot access any network interfaces 
     */
    private HashSet<InetAddress> getValidInetAddresses() throws SocketException {
        HashSet<InetAddress> ips = new HashSet<>();
        
        // get network interfaces
        Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
        NetworkInterface ni = null;
        Enumeration<InetAddress> addrs = null;
        InetAddress ia = null;
        
        while(eni.hasMoreElements()){
            try {
                ni = eni.nextElement();
                if(!ni.isLoopback() && ni.isUp()){
                    //System.out.println(ni);
                    addrs = ni.getInetAddresses();
                    while(addrs.hasMoreElements()){
                        ia = addrs.nextElement();
                        //System.out.println(ia.getHostAddress());
                        ips.add(ia);
                    }
                }
            } catch (SocketException ex) {
                // catch here so one broken socket doesn't cause the whole method to crash
                ex.printStackTrace();
            }
        }
        return ips;
    }
    
    public final List<String> getValidIps(){
        return validIpAddresses.stream().map((i)->i.getHostAddress()).collect(Collectors.toList());
    }
    
    
    public static final void validateServer() throws IOException{
        if(instance == null){
            instance = new OrpheusServer();
        }
    }
    
    /**
     * Use this method to interact with the OrpheusServer.
     * This method create the instance of OrpheusServer if it is not yet initialized.
     * 
     * I'm not sure if I want to add a different method which creates the server,
     * so it can disable multiplayer if it fails.
     * 
     * Note that you must still call .start() on the server
     * 
     * @return the instance of OrpheusServer
     */
    public static final OrpheusServer getInstance() {
        if(instance == null){
            throw new NullPointerException("Looks like you forgot to call OrpheusServer.validateServer() before OrpheusServer.getInstance()");
        }
        return instance;
    }
        
    /**
     * 
     * @return this
     * @throws java.io.IOException
     *  
     */
    public OrpheusServer start() throws IOException{
        log("Calling start method...");
        if(isStarted){
            //don't start if this is already started
            log("server is already started. Ignoring invokation.");
            return this;
        }
        
        log("Valid IP addresses include:");
        validIpAddresses.forEach(this::log);
        log(String.format("Server initialized on port %d", PORT));
        reset();
        
        isStarted = true;
        
        return this;
    }
    
    /**
     * Clears all receivers and connections from this,
     * then restarts the connection listener.
     * 
     * I will likely remove this later,
     * once I figure out how I want these
     * protocols to work.
     * 
     * @return this
     */
    public OrpheusServer reset(){
        log("Server reset");
        
        // these mess stuff up.
        //currentProtocol = null; 
        //currentChatProtocol = null;
        
        //connections.values().forEach((conn)->conn.close());
        //connections.clear();
        
        startConnListener();
        listenForConn = true;
        
        return this;
    }
    
    /**
     * If the server is not started, starts it.
     * If the server is already started, resets it.
     * @return this, for chaining purposes.
     * @throws IOException if an error occurs while starting the server
     */
    public OrpheusServer restart() throws IOException{
        log("Method restart invoked");
        if(isStarted){
            reset();
        } else {
            start();
        }
        return this;
    }
    
    public boolean isStarted(){
        return isStarted;
    }
    
    public ServerSocket getServerSocket(){
        return server;
    }
        
    private void startConnListener(){
        log("Initialize connection listener thread...");
        if(connListener == null){
            connListener = new Thread(){
                @Override
                public void run(){
                    log("Server started, waiting for client...");
                    Socket remoteComputer = null;
                    while(true){
                        try {
                            remoteComputer = server.accept();
                            log(String.format("server accepted socket %s", remoteComputer.getInetAddress().getHostAddress()));
                            if(listenForConn){
                                connect(remoteComputer);
                            } else {
                                log("Not listening for connections, so I will disconnect them");
                                remoteComputer.close();
                            }
                        } catch(SocketException ex){
                            System.out.println("Server shut down");
                            //ex.printStackTrace();
                            break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    log("done acception connections.");
                    connListener = null;
                }
            };
            connListener.start();
        } else {
            log("thread is already initialized");
        }
    }
    
    public synchronized void connect(String ipAddr) throws UnknownHostException, IOException{
        connect(InetAddress.getByName(ipAddr));
    }
    public synchronized void connect(InetAddress ipAddr) throws IOException{
        log(String.format("Connecting to %s...", ipAddr));
        //if(ipAddr.equals(this.ipAddress)){
            //log("Do not connect to self. Exiting.");
        //} else {
            Socket sock = new Socket(ipAddr, PORT);
            connect(sock);
        //}
    }
    public synchronized void connect(Socket otherComputer) throws IOException{
        logConnections();
        if(connections.containsKey(otherComputer.getInetAddress())){
            log("Already connected to " + otherComputer.getInetAddress());
            return;
        }

        log(String.format("Initializing connection to %s...", otherComputer.getInetAddress().getHostAddress()));
        Connection conn = new Connection(otherComputer);
        log("Connection successful");
        connections.put(otherComputer.getInetAddress(), conn);

        //do I need to store this somewhere?
        log("Opening message listener thread...");
        new Thread(){
            @Override
            public void run(){
                ServerMessagePacket fromClient = null;
                while(true){
                    try{
                        fromClient = conn.readServerMessage();
                        if(fromClient.getType() == ServerMessageType.SERVER_SHUTDOWN){
                            log("breaking");
                            break;
                        }
                        receiveMessage(fromClient);
                    } catch (EOFException ex){
                        ex.printStackTrace();
                        log("connection terminated");
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                log("disconnecting...");
                disconnect(otherComputer.getInetAddress());
            }
        }.start();
        log("Listener thread started successfully");
        //out.println("connected to " + otherComputer.getInetAddress().getHostAddress());

        //includes the User data so the other computer has access to username
        
        conn.writeServerMessage(new ServerMessage(
            LocalUser.getInstance().serializeJson().toString(),
            ServerMessageType.PLAYER_JOINED
        ));        
        
        log("Wrote user information to client");
        logConnections();
    }
    
    private synchronized void disconnect(InetAddress ipAddr){
        if(connections.containsKey(ipAddr)){
            connections.get(ipAddr).close();
            connections.remove(ipAddr);
        }else{
            log("not contains key " + ipAddr);
        }
    }
    
    public void send(ServerMessage sm){
        connections.values().stream().forEach((Connection c)->{
            c.writeServerMessage(sm);
        });
    }
    
    public boolean send(ServerMessage sm, InetAddress ipAddr){
        boolean success = false;
        if(connections.containsKey(ipAddr)){
            connections.get(ipAddr).writeServerMessage(sm);
            success = true;
        }
        return success;
    }
    
    private void receiveJoin(ServerMessagePacket sm){
        InetAddress ip = sm.getSendingIp();
        if(connections.containsKey(ip) && connections.get(ip).getUser() != null){
            log("already connected");
        } else if(connections.containsKey(ip)){
            //connected to IP, but no user data set yet
            AbstractUser sender = AbstractUser.deserializeJson(JsonUtil.fromString(sm.getBody()));
            if(sender instanceof RemoteUser){
                ((RemoteUser)sender).setIpAddress(sm.getSendingIp());
            }
            
            sm.setSender(sender);
            connections.get(ip).setUser(sender);
            logConnections();
        } else {
            //not connected, no user data
            try {
                connect(ip);
                AbstractUser sender = AbstractUser.deserializeJson(JsonUtil.fromString(sm.getBody()));
                if(sender instanceof RemoteUser){
                    ((RemoteUser)sender).setIpAddress(sm.getSendingIp());
                }
                sm.setSender(sender);
                connections.get(ip).setUser(sender);
                logConnections();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    
    private void receiveDisconnect(ServerMessagePacket sm){
        InetAddress ip = sm.getSendingIp();
        if(connections.containsKey(ip)){
            log(ip + " left");
            disconnect(sm.getSendingIp());
        }else{
            log(ip + " is not connected, so I cannot disconnect from them");
        }
    }
    
    public final void receiveMessage(ServerMessagePacket sm){
        if(connections.containsKey(sm.getSendingIp())){
           sm.setSender(connections.get(sm.getSendingIp()).getUser()); 
        } else {
           log("I don't recognize " + sm.getSendingIp());
        }

        // handle joining / leaving
        if(sm.getType() == ServerMessageType.PLAYER_JOINED){
            receiveJoin(sm);
        } else if (sm.getType() == ServerMessageType.PLAYER_LEFT){
            receiveDisconnect(sm);
        }

        boolean handled = false;
        if(currentProtocol == null){
            log("No current protocol :(");
        } else {
            handled = currentProtocol.receiveMessage(sm, this);
        }

        if(!handled && currentChatProtocol != null){
            handled = currentChatProtocol.receiveMessage(sm, this);
        }

        if(handled){
            //log("Successfully received!");
        } else {
            log("Nope, didn't receive properly, so I'll cache it: " + sm.getBody());
            log("(" + sm.hashCode() + ")");
            cachedMessages.add(sm);

        }
    }
    
    /**
     * @param protocol the new protocol to use. This can be null
     */
    public void setProtocol(AbstractOrpheusServerNonChatProtocol protocol){
        log(String.format("Set protocol to %s", protocol.toString()));
        currentProtocol = protocol;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(protocol.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
                log("uncached message " + sm.hashCode());
            }
        });
    }
    
    /**
     * Sets the separate protocol to receive chat 
     * messages. This way, I can't have multiple
     * regular protocols, but chat also can't interfere
     * with the other protocol!
     * 
     * ... or I could just have the other protocols
     * forward to their chat...
     * 
     * @param chat the ChatProtocol to handle messages
     * received by this server.
     */
    public void setChatProtocol(ChatProtocol chat){
        log(String.format("Set chat protocol to %s", chat.toString()));
        currentChatProtocol = chat;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(chat.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
                log("uncached message " + sm.hashCode());
            }
        });
    }
    
    public final void setAcceptingConn(boolean b){
        listenForConn = b;
        if(b){
            startConnListener();
        }
    }
    
    public final void shutDown(){
        if(!isStarted){
            return;
        }
        send(new ServerMessage(
            "server shutting down",
            ServerMessageType.SERVER_SHUTDOWN
        ));
        
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        isStarted = false;
    }
    
    public synchronized void logConnections(){
        log("CONNECTIONS:");
        connections.values().stream().forEach((Connection c)->c.displayData());
        log("END OF CONNECTIONS");
    }
    
    // make this save to a file later
    public final void log(String msg){
        System.out.println("OrpheusServer: " + msg);
    }
    
    public final void log(Object obj){
        log(obj.toString());
    }
    
    // yay! this works!
    public static void main(String[] args) throws SocketException{
        try {
            OrpheusServer os = new OrpheusServer();
            os.logConnections();
            os.start();
            new Thread(){
                @Override
                public void run(){
                    try {
                        new Connection(new Socket(InetAddress.getLoopbackAddress(), PORT));
                        //new Socket("0.0.0.0", PORT);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
            //os.shutDown();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
