package orpheus.server;

import java.io.IOException;

import orpheus.core.net.messages.Message;
import orpheus.core.net.messages.MessageType;
import orpheus.server.protocols.HubProtocol;

public class Main {
    
    public static void main(String[] args) throws IOException {
        var server = OrpheusServer.create();
        System.out.printf("Started server on %s...\n", server.getConnectionString());
        server.setProtocol(new HubProtocol());

        var client = OrpheusClient.create(server.getIpAddress(), server.getPort());
        var msg = new Message(MessageType.LIST_WAITING_ROOMS);
        client.send(msg);
        
        //server.shutDown();
    }
}
