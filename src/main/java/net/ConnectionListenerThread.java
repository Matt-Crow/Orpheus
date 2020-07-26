package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * The ConnectionListenerThread listens for remote connections
 * to the OrpheusServer.
 * Currently not used.
 * 
 * @author Matt Crow
 */
public class ConnectionListenerThread extends Thread {
    private final OrpheusServer server; // chache it here so I don't have to keep calling OrpheusServer.getInstance()
    private volatile boolean shouldListen;
    /**
     * Creates the ConnectionListenerThread, but does not
     * start it.
     * 
     * @param forServer the instance of OrpheusServer
     */
    public ConnectionListenerThread(OrpheusServer forServer){
        super();
        server = forServer;
        shouldListen = false;
        log("Initialized ConnectionListenerThread, though it has not started");
    }
    
    @Override
    public void run(){
        log("Starting ConnectionListenerThread");
        shouldListen = true;
        ServerSocket serverSock = server.getServerSocket();
        Socket remoteComputer = null;
        while(shouldListen){ // doesn't properly break out
            try {
                remoteComputer = serverSock.accept();
                log(String.format("server accepted socket %s", remoteComputer.getInetAddress().getHostAddress()));
                server.connect(remoteComputer);
            } catch(SocketException ex){
                ex.printStackTrace();
                break;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        log("done acception connections.");
    }
    
    public final void stopListening(){
        shouldListen = false;
    }
    
    private void log(String msg){
        server.log(String.format("%s: %s", this.toString(), msg));
    }
    
    public static void main(String[] args) throws IOException{
        OrpheusServer.getInstance().start();
        ConnectionListenerThread t = new ConnectionListenerThread(OrpheusServer.getInstance());
        t.start();
        
        t.stopListening();
        OrpheusServer.getInstance().shutDown();
    }
}
