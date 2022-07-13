package net.connections;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class listens for requests to connect with the server, and adds 
 * Connections to the given connections object. 
 * 
 * @author Matt Crow
 */
public class ConnectionListener {
    private final ServerSocket server;
    private final Connections connections;
    private volatile boolean listenForConnections;
    private Thread listenerThread;
    private final Consumer<Connection> onConnect;
    
    public ConnectionListener(ServerSocket requestListener, Connections connections, Consumer<Connection> onConnect){
        this.server = requestListener;
        this.connections = connections;
        listenForConnections = false;
        this.onConnect = onConnect;
    }
    
    public final void startOrContinue(){
        if(!listenForConnections){
            listenForConnections = true;
            listenerThread = createListenerThread();
            listenerThread.start();
        }
    }
    
    private Thread createListenerThread(){
        return new Thread("Orpheus server connection manager"){
            @Override
            public void run(){
                continueListening();
                System.out.println("Done listening for connections");
            }
        };
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
                client = server.accept();
                System.out.printf("Connecting to %s:%d%n", client.getInetAddress().getHostAddress(), client.getPort());
                connections.connectTo(client);
                onConnect.accept(connections.getConnectionTo(client));
            }
        } catch (SocketTimeoutException ex) {
            // this is not an error, it just means no client has attempted to connect
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
    public final void stop(){
        listenForConnections = false;
        // the associated Thread can now exit
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String status = (listenForConnections) ? "listening" : "inactive";
        sb.append(String.format("Server Connection Listener (%s):%n", status));
        sb.append(connections.toString());
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException{
        ServerSocket server = new ServerSocket(5000);
        List<Socket> clients = new LinkedList<>();

        server.setSoTimeout(3000);
        ConnectionListener manager = new ConnectionListener(server, new Connections(), System.out::println);
        manager.startOrContinue();

        for(int i = 0; i < 3; ++i){
            clients.add(new Socket(InetAddress.getLoopbackAddress(), 5000));
        }
        System.out.println(manager);
        Thread.sleep(3000);
        manager.stop();
        manager.connections.closeAll();
        manager.startOrContinue();
        manager.startOrContinue();
        clients.add(new Socket(InetAddress.getLoopbackAddress(), 5000));
        Thread.sleep(3000);
        manager.stop();
        manager.connections.closeAll();
        System.out.println("end of program");

        clients.forEach(c -> {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
