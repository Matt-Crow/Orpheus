package net;

import controllers.Master;
import controllers.User;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static java.lang.System.out;
import javax.json.Json;
import javax.json.JsonException;

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
 * and calls the corresponding Consumer in the 'receivers' HashMap.
 * 
 * @author Matt Crow
 */
public class OrpheusServer {
    private final ServerSocket server;
    private final HashMap<String, Connection> connections;
    private Thread connListener;
    private volatile boolean listenForConn;
    private OrpheusServerState state; //what this server is doing
    
    private final HashMap<Integer, Consumer<ServerMessage>> receivers; //integer is ServerMessage type
    
    public static final String SHUTDOWN_MESSAGE = "EXIT";
    public static final String SOMEONE_LEFT = "Discon: ";
    
    public OrpheusServer(int port) throws IOException{
        state = OrpheusServerState.NONE;
        receivers = new HashMap<>();
        
        try{
            server = new ServerSocket(port);
        } catch (IOException ex) {
            //make this test for another open port?
            System.err.println("Couldn't initialize server on port " + port);
            throw ex;
        }
        
        System.out.println("Server started on " + InetAddress.getLocalHost().getHostAddress());
        System.out.println(
            String.format(
                "To connect to this server, call \'new Socket(\"%s\", %d);\'", 
                InetAddress.getLocalHost().getHostAddress(), 
                port
            )
        );
        
        connections = new HashMap<>();
        listenForConn = true;
        
        startConnListener();
        initReceivers();
    }
    
    private void initReceivers(){
        receivers.put(ServerMessage.PLAYER_JOINED, (ServerMessage sm)->{
            out.println("player joined " + sm.getSenderIpAddr());
            if(connections.containsKey(sm.getSenderIpAddr())){
                out.println("already connected");
            } else {
                User u = User.deserializeJson(Json.createReader(new StringReader(sm.getBody())).readObject());
                connect(sm.getSenderIpAddr());
                connections.get(sm.getSenderIpAddr()).setUser(u);
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
        String ret = "ERROR";
        try {
            ret = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(OrpheusServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    
    public void setState(OrpheusServerState s){
        state = s;
    }
    public OrpheusServerState getState(){
        return state;
    }
    
    public synchronized void connect(String ipAddr){
        try {
            connect(new Socket(ipAddr, server.getLocalPort()));
        } catch (IOException ex) {
            Logger.getLogger(OrpheusServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private synchronized void connect(Socket otherComputer){
        try{
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
                            if(ip == null || ip.contains(SHUTDOWN_MESSAGE)){
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
                        }
                    }
                    out.println("disconnecting...");
                    disconnect(otherComputer.getInetAddress().getHostAddress());
                    out.println("closing...");
                    conn.close();
                }
            }.start();
            //out.println("connected to " + otherComputer.getInetAddress().getHostAddress());
            
            //includes the User data so the other computer has access to username
            conn.writeToClient(new ServerMessage(
                Master.getUser().serializeJson().toString(),
                ServerMessage.PLAYER_JOINED
            ).toJsonString());
            
        } catch (IOException ex) {
            System.err.println("Failed to connect to client");
            ex.printStackTrace();
        }
    }
    private synchronized void disconnect(String ipAddr){
        if(connections.containsKey(ipAddr)){
            try {
                out.println("sending someone left " + ipAddr);
                connections.get(ipAddr).writeToClient(SOMEONE_LEFT + getIpAddr());
                connections.remove(ipAddr);
            } catch (IOException ex) {
                Logger.getLogger(OrpheusServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            out.println("not contains key " + ipAddr);
        }
    }
    
    public void send(String msg){
        connections.values().stream().forEach((Connection c)->{
            try {
                c.writeToClient(msg);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void send(ServerMessage sm){
        connections.values().stream().forEach((Connection c)->{
            try {
                c.writeToClient(sm.toJsonString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void receive(String msg){
        boolean dealtWith = false; //can get rid of this once I'm done with the switch statement
        try{
            ServerMessage sm = ServerMessage.deserializeJson(msg);
            
            if(receivers.containsKey(sm.getType())){
                receivers.get(sm.getType()).accept(sm);
                dealtWith = true;
            } else {
                dealtWith = false;
            }
        } catch (JsonException ex){
            out.println("nope. not server message");
            ex.printStackTrace();
        }
        
        if(dealtWith){
            return;
        }
        
        out.println("Received " + msg.toUpperCase());
        if(msg.toUpperCase().contains(SOMEONE_LEFT.toUpperCase())){
            String ipAddr = msg.replace(SOMEONE_LEFT, "");
            out.println(ipAddr + " left"); 
            if(connections.containsKey(ipAddr)){
                out.println("disconnect");
                disconnect(ipAddr);
            }else{
                out.println("no disconnect");
            }
        }else{
            out.println("What do I do with this? " + msg);
        }
    }
    
    /**
     * 
     * @param key the type of ServerMessage this should receive. See ServerMessage for possible values this may have
     * @param nomNom the function to run upon receiving a ServerMessage of the given type. 
     * @see ServerMessage
     */
    public void setReceiverFunction(int key, Consumer<ServerMessage> nomNom){
        receivers.put(key, nomNom);
    }
    
    public final void setAcceptingConn(boolean b){
        listenForConn = b;
        if(b){
            startConnListener();
        }
    }
    
    public final void shutDown(){
        connections.values().stream().forEach((Connection c)->{
            try {
                c.writeToClient(SHUTDOWN_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public synchronized void logConnections(){
        out.println("CONNECTIONS:");
        connections.keySet().forEach((ipAddr)->out.println(ipAddr));
        out.println("END OF CONNECTIONS");
    }
    
    public static void main(String[] args){
        try {
            OrpheusServer os = new OrpheusServer(5000);
            String ip = JOptionPane.showInputDialog("enter host ip address to connect to");
            if(ip != null){
                os.connect(ip);
                os.send("hello?");
                os.send("is this thing on?");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
