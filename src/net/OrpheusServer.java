package net;

import java.io.EOFException;
import java.io.IOException;
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
 * @author Matt Crow
 */
public class OrpheusServer {
    private final ServerSocket server;
    private final HashMap<String, Connection> connections;
    private Thread connListener;
    private volatile boolean listenForConn;
    private OrpheusServerState state; //what this server is doing
    private Consumer<String> receiver; //messages sent to the server are fed into this
    
    public static final String SHUTDOWN_MESSAGE = "EXIT";
    public static final String SOMEONE_JOINED = "Conn: ";
    public static final String SOMEONE_LEFT = "Discon: ";
    
    public OrpheusServer(int port) throws IOException{
        state = OrpheusServerState.NONE;
        receiver = (String s)->System.out.println(s);
        
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
    
    //todo make this change how it reacts to receiving messages?
    public void setState(OrpheusServerState s){
        state = s;
    }
    public OrpheusServerState getState(){
        return state;
    }
    
    //TODO add ability to disconnect
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
            out.println("connected to " + otherComputer.getInetAddress().getHostAddress());
            out.println("Writing to client: joined " + getIpAddr());
            conn.writeToClient(SOMEONE_JOINED + getIpAddr());
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
    
    public void receive(String msg){
        out.println("Received " + msg.toUpperCase());
        if(msg.toUpperCase().contains(SOMEONE_JOINED.toUpperCase())){
            logConnections();
            String ipAddr = msg.replace(SOMEONE_JOINED, "");
            //out.println(ipAddr + " joined"); 
            if(!connections.containsKey(ipAddr)){
                out.println("connect");
                connect(ipAddr);
            }else{
                out.println("already connected");
            }
        }else if(msg.toUpperCase().contains(SOMEONE_LEFT.toUpperCase())){
            String ipAddr = msg.replace(SOMEONE_LEFT, "");
            out.println(ipAddr + " left"); 
            if(connections.containsKey(ipAddr)){
                out.println("disconnect");
                disconnect(ipAddr);
            }else{
                out.println("no disconnect");
            }
        }else{
            receiver.accept(msg);
        }
    }
    
    public void setReceiverFunction(Consumer<String> nomNom){
        receiver = nomNom;
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
