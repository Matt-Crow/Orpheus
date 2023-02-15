package orpheus.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import orpheus.core.net.messages.Message;
import orpheus.server.connections.Connection;

/**
 * connects to a remote OrpheusServer to exchange messages
 */
public class OrpheusClient {
    
    /**
     * the connection to the OrpheusServer
     */
    private final Connection connection;

    private OrpheusClient(Connection connection) {
        this.connection = connection;
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
     * sends a message to the server
     * @param message the message to send
     * @throws IOException if an error occurs while sending to the server
     */
    public void send(Message message) throws IOException {
        connection.send(message);
    }
}
