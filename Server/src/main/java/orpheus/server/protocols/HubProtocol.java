package orpheus.server.protocols;

import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;

import orpheus.core.net.SocketAddress;
import orpheus.core.net.WaitingRoom;
import orpheus.core.net.connections.Connection;
import orpheus.core.net.messages.Message;
import orpheus.core.net.messages.MessageType;
import orpheus.core.users.User;
import orpheus.server.OrpheusServer;

/**
 * allows clients to get & create waiting rooms
 */
public class HubProtocol implements ServerProtocol {

    @Override
    public void receive(OrpheusServer server, Connection from, Message message) {
        switch (message.getMessageType()) {
            case LIST_WAITING_ROOMS: {
                var response = makeWaitingRoomListMessage();
                try {
                    from.send(response);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case NEW_WAITING_ROOM: {
                break;
            }
            default:
                break;
        }
    }

    private Message makeWaitingRoomListMessage() {
        // todo read from list of waiting rooms & associated servers
        // they would not use same port as server received
        var dummy = new WaitingRoom(new SocketAddress("123:45:67:89", 1234));
        dummy.addUser(new User("Foo"));
        dummy.addUser(new User("Bar"));

        var rooms = new ArrayList<WaitingRoom>();
        rooms.add(dummy);

        var roomsJson = Json.createArrayBuilder();
        for (var room : rooms) {
            roomsJson.add(room.serializeJson());
        }

        var json = Json.createObjectBuilder()
            .add("rooms", roomsJson.build())
            .build();
        
        return new Message(MessageType.LIST_WAITING_ROOMS, json);
    }
}
