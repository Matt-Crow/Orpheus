package net;

import net.protocols.AbstractOrpheusServerNonChatProtocol;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.net.InetSocketAddress;
import java.net.SocketException;
import javax.json.JsonException;
import net.protocols.ChatProtocol;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
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
    private boolean isStarted;
    private ServerSocket server;
    private String ipAddress;
    
    /*
    The users connected to this server, where the key is their
    IP address.
    */
    private final HashMap<String, Connection> connections;
    private Thread connListener; //the thread that listens for attemts to connect to this server
    private volatile boolean listenForConn; //whether or not the connListener thread is active
    
    private final SafeList<ServerMessage> cachedMessages; //messages received before the receiver could be
    
    private volatile AbstractOrpheusServerNonChatProtocol currentProtocol;
    private volatile ChatProtocol currentChatProtocol;
    
    public static final int PORT = 5000;
    private static OrpheusServer instance = null;
    
    /**
     * Creates an OrpheusServer.
     * Note that this does not actually start the server,
     * you need to call start() for that.
     */
    private OrpheusServer(){
        if(instance != null){
            throw new ExceptionInInitializerError("OrpheusServer is a singleton class: Use OrpheusServer.getInstance()");
        }
        ipAddress = "127.0.0.1"; // Loopback address. This is just a default value
        connections = new HashMap<>();
        cachedMessages = new SafeList<>();
        listenForConn = false;   
        isStarted = false;
        currentProtocol = null;
        currentChatProtocol = null;
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
            instance = new OrpheusServer();
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
        
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        server = new ServerSocket(PORT);
        log(String.format("Server initialized on %s:%d", ipAddress, PORT));
        log("InetAddress.getLocalHost().getHostAddress(): " + InetAddress.getLocalHost().getHostAddress());
        log("server.getInetAddress().getHostName(): " + server.getInetAddress().getHostName());
        log("server.toString(): " + server.toString());;
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
        
    private void startConnListener(){
        log("Initialize connection listener thread...");
        if(connListener == null){
            connListener = new Thread(){
                @Override
                public void run(){
                    log("Server started, waiting for client...");
                    Socket remoteComputer = null;
                    while(listenForConn){
                        try {
                            remoteComputer = server.accept();
                            log(String.format("server accepted socket %s", remoteComputer.getInetAddress().getHostAddress()));
                            connect(remoteComputer);
                        } catch(SocketException ex){
                            ex.printStackTrace();
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
    
    
    
    public String getIpAddr(){
        return ipAddress;
    }
    
    public synchronized void connect(String ipAddr) throws IOException{
        log(String.format("Connecting to %s...", ipAddr));
        if(ipAddr.equals(this.ipAddress)){
            log("Do not connect to self. Exiting.");
        } else {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(ipAddr, PORT), 3000);
            connect(sock);
        }
    }
    private synchronized void connect(Socket otherComputer) throws IOException{
        logConnections();
        if(connections.containsKey(otherComputer.getInetAddress().getHostAddress())){
            log("Already connected to " + otherComputer.getInetAddress().getHostAddress());
            return;
        }

        log(String.format("Initializing connection to %s...", otherComputer.getInetAddress().getHostAddress()));
        Connection conn = new Connection(otherComputer);
        log("Connection successful");
        connections.put(otherComputer.getInetAddress().getHostAddress(), conn);

        //do I need to store this somewhere?
        log("Opening message listener thread...");
        new Thread(){
            @Override
            public void run(){
                String ip = "";
                while(true){
                    try{
                        ip = conn.readFromClient();
                        if(ip == null || ip.toUpperCase().contains(ServerMessageType.SERVER_SHUTDOWN.toString().toUpperCase())){
                            log("breaking");
                            break;
                        }
                        receive(ip);

                    } catch (EOFException ex){
                        ex.printStackTrace();
                        log("connection terminated");
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                log("disconnecting...");
                disconnect(otherComputer.getInetAddress().getHostAddress());
            }
        }.start();
        log("Listener thread started successfully");
        //out.println("connected to " + otherComputer.getInetAddress().getHostAddress());

        //includes the User data so the other computer has access to username
        conn.writeToClient(new ServerMessage(
            LocalUser.getInstance().serializeJson().toString(),
            ServerMessageType.PLAYER_JOINED
        ).toJsonString());
        log("Wrote user information to client");
        logConnections();
    }
    
    private synchronized void disconnect(String ipAddr){
        if(connections.containsKey(ipAddr)){
            connections.get(ipAddr).close();
            connections.remove(ipAddr);
        }else{
            log("not contains key " + ipAddr);
        }
    }
    
    public void send(String msg){
        connections.values().stream().forEach((Connection c)->{
            c.writeToClient(msg);
        });
    }
    
    public void send(ServerMessage sm){
        send(sm.toJsonString());
    }
    
    public boolean send(ServerMessage sm, String ipAddr){
        boolean success = false;
        if(connections.containsKey(ipAddr)){
            connections.get(ipAddr).writeToClient(sm.toJsonString());
            success = true;
        }
        return success;
    }
    
    private void receiveJoin(ServerMessage sm){
        String ip = sm.getIpAddr();
        if(connections.containsKey(ip) && connections.get(ip).getUser() != null){
            log("already connected");
        } else if(connections.containsKey(ip)){
            //connected to IP, but no user data
            connections.get(ip).setUser(AbstractUser.deserializeJson(JsonUtil.fromString(sm.getBody())));
            logConnections();
        } else {
            //not connected, no user data
            try {
                connect(ip);
                connections.get(ip).setUser(AbstractUser.deserializeJson(JsonUtil.fromString(sm.getBody())));
                logConnections();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    
    private void receiveDisconnect(ServerMessage sm){
        String ip = sm.getSender().getIpAddress();
        if(connections.containsKey(ip)){
            log(ip + " left");
            disconnect(sm.getIpAddr());
        }else{
            log(ip + " is not connected, so I cannot disconnect from them");
        }
    }
    
    public void receive(String msg){
        try{
            ServerMessage sm = ServerMessage.deserializeJson(msg);
            if(connections.containsKey(sm.getIpAddr())){
               sm.setSender(connections.get(sm.getIpAddr()).getUser()); 
            } else {
               log("I don't recognize " + sm.getIpAddr());
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
                log("Successfully received!");
            } else {
                log("Nope, didn't receive properly, so I'll cache it: " + msg);
                log("(" + sm.hashCode() + ")");
                cachedMessages.add(sm);
                
            }
            
        } catch (JsonException ex){
            log("nope. not server message: " + msg);
            ex.printStackTrace();
        }
    }
    
    /**
     * @param protocol the new protocol to use. This can be null
     */
    public void setProtocol(AbstractOrpheusServerNonChatProtocol protocol){
        log(String.format("Set protocol to %s", protocol.toString()));
        currentProtocol = protocol;
        cachedMessages.forEach((ServerMessage sm)->{
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
        cachedMessages.forEach((ServerMessage sm)->{
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
    private void log(String msg){
        System.out.println("OrpheusServer: " + msg);
    }
    
    private void log(Object obj){
        log(obj.toString());
    }
    
    // yay! this works!
    public static void main(String[] args){
        try {
            OrpheusServer os = new OrpheusServer();
            os.logConnections();
            os.start();
            new Thread(){
                @Override
                public void run(){
                    try {
                        new Connection(new Socket(os.getIpAddr(), PORT));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }.start();
            os.shutDown();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
