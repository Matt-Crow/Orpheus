package orpheus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import orpheus.core.net.messages.Message;
import orpheus.server.connections.Connection;
import orpheus.server.connections.ConnectionListenerThread;
import orpheus.server.connections.MessageListenerThread;

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
        this.listenerThread = ConnectionListenerThread.spawn(socket, this::connect);
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

    private void connect(Socket client) {
        try {
            tryConnect(client);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void tryConnect(Socket client) throws IOException {
        var connection = Connection.connect(client);
        var t = new MessageListenerThread(connection, this::receive);
        // todo save t for later
    }

    private void receive(Message message) {
        System.out.printf("Server received %s", message.toString());
    }
}
