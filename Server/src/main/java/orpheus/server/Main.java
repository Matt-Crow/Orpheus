package orpheus.server;

import java.io.IOException;

import orpheus.core.net.messages.Message;
import orpheus.core.net.messages.MessageType;

public class Main {
    
    public static void main(String[] args) throws IOException {
        var server = OrpheusServer.create();
        System.out.printf("Started server on %s...\n", server.getConnectionString());

        var client = OrpheusClient.create(server.getIpAddress(), server.getPort());
        var msg = new Message(MessageType.NEW_WAITING_ROOM);
        client.send(msg);
    }
}
