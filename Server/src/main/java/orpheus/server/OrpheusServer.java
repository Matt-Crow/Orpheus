package orpheus.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

import orpheus.core.net.messages.Message;
import orpheus.core.net.messages.MessageType;
import orpheus.server.connections.Connection;
import orpheus.server.connections.ConnectionListenerThread;
import orpheus.server.connections.Connections;
import orpheus.server.connections.MessageListenerThread;
import orpheus.server.protocols.Protocol;

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

    /**
     * the collection of connections this has established with clients
     */
    private final Connections connections;

    /**
     * the current protocol running on this server
     */
    private Optional<Protocol> protocol;

    /*
    The amount of time a call to server.accept() will block for
    */
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    private OrpheusServer(ServerSocket socket) {
        this.socket = socket;
        listenerThread = ConnectionListenerThread.spawn(socket, this::connect);
        connections = new Connections();
        protocol = Optional.empty();
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

    /**
     * configures the server to handle messages using the given protocol
     * @param protocol the new protocol to use
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = Optional.of(protocol);
    }

    public void shutDown() {
        connections.closeAll();
        listenerThread.terminate(); // also handles the socket
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
        connections.add(connection);
    }

    private void receive(Connection sender, Message message) {
        System.out.printf("Server received %s", message.toString());
        if (message.getMessageType() == MessageType.CLOSE_CONNECTION) {
            connections.close(sender);
        }
        if (protocol.isPresent()) {
            protocol.get().receive(this, sender, message);
        }
    }
}
