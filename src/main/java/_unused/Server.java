package _unused;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.Connection;

/**
 * A Server is used to open certain features of Orpheus to other computers.
 * It works alongside the Client class to manage connections.
 * Since Orpheus is peer-to-peer at the moment, I'm not sure if Server and Client should just be one class.
 * @author Matt Crow.
 */
public class Server {
    private ServerSocket server;
    private final ArrayList<Connection> clientConnections;
    private Thread t; //listens for new connections
    private volatile boolean acceptingConn;
    public static final String SHUTDOWN_MESSAGE = "EXIT";
    
    public Server(int port) throws IOException{
        server = new ServerSocket(port);
        System.out.println("Server started on " + InetAddress.getLocalHost().getHostAddress());
        System.out.println(
            String.format(
                "To connect to this server, call \'new Socket(\"%s\", %d);\'", 
                InetAddress.getLocalHost().getHostAddress(), 
                port
            )
        );
        
        clientConnections = new ArrayList<>();
        acceptingConn = true;
        
        t = new Thread(){
            @Override
            public void run(){
                try{
                    System.out.println("Server started, waiting for client...");
                    
                    while(acceptingConn){
                        connect(server.accept());
                    }
                    System.out.println("done acception connections.");
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }
    
    private void connect(Socket otherComputer){
        try{
            Connection conn = new Connection(otherComputer);
            clientConnections.add(conn);

            //do I need to store this somewhere?
            new Thread(){
                @Override
                public void run(){
                    String ip = "";
                    while(ip != null && !"done".equals(ip)){
                        /*
                        try{
                            ip = conn.readFromClient();
                            System.out.println(ip);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }*/
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
        clientConnections.stream().forEach((Connection c)->{
            //c.writeToClient(msg);
        });
    }
    
    //todo make this restart the listening server if it was shut down
    public final void setAcceptingConn(boolean b){
        acceptingConn = b;
    }
    
    public final void shutDown(){
        clientConnections.stream().forEach((Connection c)->{
            //c.writeToClient(SHUTDOWN_MESSAGE);
        });
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        try {
            new Server(5000);
        } catch (IOException ex) {
            System.err.println("failed to start server");
            ex.printStackTrace();
        }
    }
}
