package net;

import java.io.IOException;

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
    
    public final OrpheusClient createClient(String hostIp, int hostPort) throws IOException {
        OrpheusClient client = new OrpheusClient(hostIp, hostPort);
        client.start();
        return client;
    }
}
