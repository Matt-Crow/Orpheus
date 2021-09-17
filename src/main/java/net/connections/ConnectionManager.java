package net.connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This class handles multiple connections to a server, creating and deleting
 * connections, as well as sending and receiving messages. Should probably not
 * inherit from Thread
 * 
 * @author Matt Crow
 */
public class ConnectionManager {
    private final ServerSocket requestListener;
    private final Connections connections;
    private volatile boolean listenForConnections;
    private final Thread connectionListenerThread;
    
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    protected ConnectionManager(ServerSocket requestListener, Connections connections){
        super();
        this.requestListener = requestListener;
        this.connections = connections;
        listenForConnections = false;
        // will need separate connection and disconnection listeners
        
        connectionListenerThread = createListenerThread();
    }
    
    private Thread createListenerThread(){
        return new Thread("Orpheus server connection manager"){
            @Override
            public void run(){
                continueListening();
            }
        };
    }
    
    public final void shutDown(){
        listenForConnections = false;
    }
    
    public final void start(){
        listenForConnections = true;
        this.connectionListenerThread.start();
    }
    
    private void continueListening(){
        while(listenForConnections){
            waitForConnection();
        }
    }
    
    private void waitForConnection(){
        try {
            Socket client;
            while(true){ // runs until accept times out
                client = requestListener.accept();
                connections.connectTo(client);
            }
        } catch (SocketTimeoutException ex) {
            // this is not an error, it just means no client has attempted to connect
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Server Connection Manager:\n");
        sb.append(connections.toString());
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        ServerSocket server = new ServerSocket(5000);
        server.setSoTimeout(CONNECTION_TIME_OUT);
        ConnectionManager manager = new ConnectionManager(server, new Connections());
        manager.start();
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        new Socket(InetAddress.getLoopbackAddress(), 5000);
        System.out.println(manager);
        Thread.sleep(3000);
        manager.shutDown();
        manager.connections.closeAll();
        System.out.println("end of program");
    }
}
