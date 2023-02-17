package orpheus.core.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import orpheus.core.net.connections.Connection;
import orpheus.core.net.messages.Message;

/**
 * connects to a remote OrpheusServer to exchange messages
 */
public class OrpheusClient {
    
    /**
     * the connection to the OrpheusServer
     */
    private final Connection connection;

    /**
     * the protocol this uses to handle messages
     */
    private Optional<ClientProtocol> protocol;

    private OrpheusClient(Connection connection) {
        this.connection = connection;
        protocol = Optional.empty();
        connection.addMessageListener(this::handleMessage);
    }

    /**
     * Creates a new client for a server hosted on the given IP address and port
     * @param ip the IP address of the host server
     * @param port the port of the host server
     * @return a new client, ready to communicate with the server
     * @throws UnknownHostException if an invalid host is given
     * @throws IOException on any IO problems
     */
    public static OrpheusClient create(String ip, int port) throws UnknownHostException, IOException {
        var socket = new Socket(ip, port);
        var connection = Connection.connect(socket);
        var client = new OrpheusClient(connection);
        return client;
    }

    /**
     * sets the new protocol to use
     * @param protocol the new protocol to use
     */
    public void setProtocol(ClientProtocol protocol) {
        this.protocol = Optional.of(protocol);
    }

    /**
     * sends a message to the server
     * @param message the message to send
     * @throws IOException if an error occurs while sending to the server
     */
    public void send(Message message) throws IOException {
        connection.send(message);
    }

    /**
     * closes the connection to the server
     * @throws IOException
     */
    public void disconnect() throws IOException {
        connection.close();
    }

    private void handleMessage(Connection from, Message message) {
        System.out.printf("Client received %s", message.toString());
        if (protocol.isPresent()) {
            protocol.get().receive(this, from, message);
        }
    }
}
