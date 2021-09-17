package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * This class handles multiple connections to a server, creating and deleting
 * connections, as well as sending and receiving messages. Should probably not
 * inherit from Thread
 * 
 * @author Matt Crow
 */
public class ConnectionManager extends Thread {
    private final ServerSocket requestListener;
    private volatile boolean listenForConnections;
    private final HashMap<Socket, Connection> connections;
    
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    protected ConnectionManager(ServerSocket requestListener){
        super();
        setName("Orpheus server connection manager");
        this.requestListener = requestListener;
        try {
            requestListener.setSoTimeout(CONNECTION_TIME_OUT);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        listenForConnections = false;
        connections = new HashMap<>();
    }
    
    public final void shutDown(){
        listenForConnections = false;
    }
    
    @Override
    public final void start(){
        listenForConnections = true;
        super.start();
    }
    
    @Override
    public final void run(){
        while(listenForConnections){
            waitForConnection();
        }
    }
    
    private void waitForConnection(){
        try {
            Socket client;
            while(true){ // runs until accept times out
                client = requestListener.accept();
                connections.put(client, new Connection(client));
                System.out.println(connections.get(client));
            }
        } catch (SocketTimeoutException ex) {
            // this is not an error, it just means no client has attempted to connect
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        ServerSocket server = new ServerSocket(5000);
        ConnectionManager manager = new ConnectionManager(server);
        manager.start();
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        Thread.sleep(3000);
        manager.shutDown();
        System.out.println("end of program");
    }
}
