package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Use this to manage all server stuffs
 * that way I don't have to open tons of connections for chat, waiting room, match, etc.
 * @author Matt Crow
 */
public class OrpheusServer {
    private final ServerSocket server;
    private final ArrayList<Connection> connections;
    private Thread connListener;
    private volatile boolean listenForConn;
    public static final String SHUTDOWN_MESSAGE = "EXIT";
    
    public OrpheusServer(int port) throws IOException{
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
        
        connections = new ArrayList<>();
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
    
    private void connect(String ipAddr){
        try {
            connect(new Socket(ipAddr, server.getLocalPort()));
        } catch (IOException ex) {
            Logger.getLogger(OrpheusServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connect(Socket otherComputer){
        try{
            Connection conn = new Connection(otherComputer);
            connections.add(conn);

            //do I need to store this somewhere?
            new Thread(){
                @Override
                public void run(){
                    String ip = "";
                    while(ip != null && !SHUTDOWN_MESSAGE.equals(ip)){
                        try{
                            ip = conn.readFromClient();
                            receive(ip);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    conn.close();
                }
            }.start();
            System.out.println("connected to " + otherComputer.getInetAddress().getHostAddress());
        } catch (IOException ex) {
            System.err.println("Failed to connect to client");
            ex.printStackTrace();
        }
    }
    
    public void send(String msg){
        connections.stream().forEach((Connection c)->{
            try {
                c.writeToClient(msg);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public void receive(String msg){
        System.out.println(msg);
    }
    
    public final void setAcceptingConn(boolean b){
        listenForConn = b;
        if(b){
            startConnListener();
        }
    }
    
    public final void shutDown(){
        connections.stream().forEach((Connection c)->{
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
