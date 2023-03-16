package net;

import java.io.IOException;

import orpheus.core.net.SocketAddress;
import orpheus.core.users.LocalUser;

/**
 * Allows easier access to creating and starting servers, but is mostly unneeded
 * @author Matt Crow
 */
public class ServerProvider {

    /**
     * Creates a server which will host games
     * @return the host server
     * @throws IOException if the server fails to open a server socket
     */
    public final OrpheusServer createHost() throws IOException {
        var server = new OrpheusServer();
        server.start();
        return server;
    }
    
    /**
     * @param user the user this client represents
     * @param address the socket address of the server this client should 
     *  connect to
     * @return a client connected to the server on the given address
     * @throws IOException if the client fails to connect
     */
    public final OrpheusClient createClient(LocalUser user, SocketAddress address) throws IOException {
        var client = new OrpheusClient(user, address);
        client.start();
        return client;
    }
}
