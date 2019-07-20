package net;

import controllers.Master;
import controllers.User;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import static java.lang.System.out;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.json.JsonException;
import serialization.JsonUtil;
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
 * @see OrpheusServer#receive() 
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
    
    private final HashMap<ServerMessageType, SafeList<Consumer<ServerMessage>>> receivers; //see the receive method
    
    public static final int PORT = 5000;
    
    public OrpheusServer(){
        receivers = new HashMap<>();
        connections = new HashMap<>();
        listenForConn = false;
        ipAddress = "server not started yet";
        
        /*
        System.out.println("Server started on " + ipAddress);
        System.out.println(
            String.format(
                "To connect to this server, call \'new Socket(\"%s\", %d);\'", 
                ipAddress, 
                port
            )
        );*/
        
        isStarted = false;
        
        //startConnListener();
        //initReceivers();
    }
    
    /**
     * 
     * @return this
     * @throws java.net.UnknownHostException 
     */
    public OrpheusServer start() throws UnknownHostException, IOException{
        if(isStarted){
            //don't start if this is already started
            return this;
        }
        
        receivers.clear();
        connections.keySet().forEach((ip)->disconnect(ip));
        initReceivers();
        
        server = new ServerSocket(PORT);
        ipAddress = InetAddress.getLocalHost().getHostAddress();
        startConnListener();
        listenForConn = true;
        
        isStarted = true;
        
        return this;
    }
    
    public boolean isStarted(){
        return isStarted;
    }
    
    //where do I need to send user data?
    private void initReceivers(){
        addReceiver(ServerMessageType.PLAYER_JOINED, (ServerMessage sm)->{
            String ip = sm.getIpAddr();
            if(connections.containsKey(ip) && connections.get(ip).getUser() != null){
                out.println("already connected");
            } else if(connections.containsKey(ip)){
                //connected to IP, but no user data
                connections.get(ip).setUser(User.deserializeJson(JsonUtil.fromString(sm.getBody())));
                logConnections();
            } else {
                //not connected, no user data
                try {
                    connect(ip);
                    connections.get(ip).setUser(User.deserializeJson(JsonUtil.fromString(sm.getBody())));
                    logConnections();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        addReceiver(ServerMessageType.PLAYER_LEFT, (ServerMessage sm)->{
            String ip = sm.getSender().getIpAddress();
            if(connections.containsKey(ip)){
                out.println(ip + " left");
                disconnect(sm.getIpAddr());
            }else{
                out.println(ip + " is not connected, so I cannot disconnect from them");
            }
        });
    }
    
    private void startConnListener(){
        if(connListener == null){
            connListener = new Thread(){
                @Override
                public void run(){
                    System.out.println("Server started, waiting for client...");
                    
                    while(listenForConn){
                        try {
                            connect(server.accept());
                        } catch(SocketException ex){
                            ex.printStackTrace();
                            break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    System.out.println("done acception connections.");
                    connListener = null;
                }
            };
            connListener.start();
        }
    }
    
    
    
    public String getIpAddr(){
        return ipAddress;
    }
    
    public synchronized void connect(String ipAddr) throws IOException{
        Socket sock = new Socket();
        sock.connect(new InetSocketAddress(ipAddr, PORT), 3000);
        connect(sock);
        
    }
    private synchronized void connect(Socket otherComputer) throws IOException{
        logConnections();
        if(connections.containsKey(otherComputer.getInetAddress().getHostAddress())){
            out.println("Already connected to " + otherComputer.getInetAddress().getHostAddress());
            return;
        }

        Connection conn = new Connection(otherComputer);
        connections.put(otherComputer.getInetAddress().getHostAddress(), conn);

        //do I need to store this somewhere?
        new Thread(){
            @Override
            public void run(){
                String ip = "";
                while(true){
                    try{
                        ip = conn.readFromClient();
                        if(ip == null || ip.toUpperCase().contains(ServerMessageType.SERVER_SHUTDOWN.toString().toUpperCase())){
                            System.out.println("breaking");
                            break;
                        }
                        receive(ip);

                    } catch (EOFException ex){
                        ex.printStackTrace();
                        out.println("connection terminated");
                        break;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                out.println("disconnecting...");
                disconnect(otherComputer.getInetAddress().getHostAddress());
            }
        }.start();
        //out.println("connected to " + otherComputer.getInetAddress().getHostAddress());

        //includes the User data so the other computer has access to username
        conn.writeToClient(new ServerMessage(
            Master.getUser().serializeJson().toString(),
            ServerMessageType.PLAYER_JOINED
        ).toJsonString());
        logConnections();
    }
    
    private synchronized void disconnect(String ipAddr){
        if(connections.containsKey(ipAddr)){
            connections.get(ipAddr).close();
            connections.remove(ipAddr);
        }else{
            out.println("not contains key " + ipAddr);
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
    
    public void receive(String msg){
        try{
            ServerMessage sm = ServerMessage.deserializeJson(msg);
            if(connections.containsKey(sm.getIpAddr())){
               sm.setSender(connections.get(sm.getIpAddr()).getUser()); 
            } else {
                out.println("I don't recognize " + sm.getIpAddr());
            }
            
            if(receivers.containsKey(sm.getType())){
                receivers.get(sm.getType()).forEach((c)->c.accept(sm));
            } else {
                out.println("What do I do with this? " + msg);
            }
        } catch (JsonException ex){
            out.println("nope. not server message");
            ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param key the type of ServerMessage this should receive.
     * @param nomNom the function to run upon receiving a ServerMessage of the given type. 
     * @see ServerMessage
     */
    public void addReceiver(ServerMessageType key, Consumer<ServerMessage> nomNom){
        if(nomNom == null){
            throw new NullPointerException();
        }
        if(!receivers.containsKey(key)){
            receivers.put(key, new SafeList<Consumer<ServerMessage>>());
        }
        receivers.get(key).add(nomNom);
    }
    
    public boolean removeReceiver(ServerMessageType type, Consumer<ServerMessage> nonNom){
        boolean wasRemoved = false;
        if(receivers.containsKey(type)){
            wasRemoved = receivers.get(type).remove(nonNom);
        }
        return wasRemoved;
    }
    
    public final void setAcceptingConn(boolean b){
        listenForConn = b;
        if(b){
            startConnListener();
        }
    }
    
    public final void shutDown(){
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
        out.println("CONNECTIONS:");
        connections.values().stream().forEach((Connection c)->c.displayData());
        out.println("END OF CONNECTIONS");
    }
    
    public synchronized void logReceivers(){
        out.println("RECEIVERS:");
        receivers.forEach((smt, sl)->{
            out.println(smt.toString() + ": X" + sl.length());
        });
        out.println("END OF RECEIVERS");
    }
    
    public static void main(String[] args){
        try {
            OrpheusServer os = new OrpheusServer();
            os.logConnections();
            os.logReceivers();
            os.start();
            os.logConnections();
            os.logReceivers();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
