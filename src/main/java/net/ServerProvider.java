package net;

import java.io.IOException;
import java.net.Socket;

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
    
    public final OrpheusServer createClient(String hostIp, int hostPort) throws IOException {
        OrpheusServer server = createHost();
        Socket host = new Socket(hostIp, hostPort);
        server.connect(host);
        return server;
    }
}
