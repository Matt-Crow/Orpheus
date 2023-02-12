package net;

import java.io.IOException;

import orpheus.core.users.LocalUser;

/**
 *
 * @author Matt Crow
 */
public class ServerProvider {
    public final OrpheusServer createHost() throws IOException {
        OrpheusServer server = new OrpheusServer();
        server.start();
        return server;
    }
    
    public final OrpheusClient createClient(LocalUser user, String hostIp, int hostPort) throws IOException {
        OrpheusClient client = new OrpheusClient(user, hostIp, hostPort);
        client.start();
        return client;
    }
}
