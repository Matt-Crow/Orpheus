package orpheus.server;

import java.io.IOException;
import java.net.ServerSocket;

import orpheus.server.connections.ConnectionListenerThread;

/**
 * waits for connections, then directs the client to a waiting room
 */
public class OrpheusServer {
    
    /**
     * listens for connections to the server
     */
    private final ServerSocket socket;

    /**
     * handles connections to the server
     */
    private final ConnectionListenerThread listenerThread;

    /*
    The amount of time a call to server.accept() will block for
    */
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    private OrpheusServer(ServerSocket socket) {
        this.socket = socket;
        this.listenerThread = ConnectionListenerThread.spawn(socket);
    }

    /**
     * creates & starts a server
     * @return the created server
     * @throws IOException if it fails to open a socket
     */
    public static OrpheusServer create() throws IOException {
        var socket = new ServerSocket(0); // 0 means "use any available port"
        socket.setSoTimeout(CONNECTION_TIME_OUT);
        var server = new OrpheusServer(socket);
        return server;
    }

    public void shutDown() {
        this.listenerThread.terminate(); // also handles the socket
    }

    /**
     * @return the address of the server in the form IP:PORT 
     */
    public String getConnectionString(){
        return String.format(
            "%s:%d", 
            socket.getInetAddress().getHostAddress(), 
            socket.getLocalPort()
        );
    }

    /**
     * @return the IPv4 address of the socket this is running on
     */
    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    /**
     * @return the port of the socket this is running on
     */
    public int getPort() {
        return socket.getLocalPort();
    }
}
