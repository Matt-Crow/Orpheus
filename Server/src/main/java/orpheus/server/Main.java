package orpheus.server;

import java.io.IOException;

import net.messages.ServerMessageType;
import orpheus.core.net.OrpheusClient;
import orpheus.core.net.SocketAddress;
import orpheus.core.net.messages.Message;
import orpheus.server.protocols.HubProtocol;

public class Main {
    
    public static void main(String[] args) throws IOException {
        var server = OrpheusServer.create();
        System.out.printf("Started server on %s...\n", server.getConnectionString());
        server.setProtocol(new HubProtocol());

        var client = OrpheusClient.create(new SocketAddress(server.getIpAddress(), server.getPort()));
        client.send(new Message(ServerMessageType.LIST_WAITING_ROOMS));
        client.send(new Message(ServerMessageType.NEW_WAITING_ROOM));
        
        //server.shutDown();
    }
}
