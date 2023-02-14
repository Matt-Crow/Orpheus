package orpheus.core.net.messages;

import java.util.Arrays;

/**
 * The types of message that can be received from a host server.
 * Not to be confused with messages received in a waiting room or in game.
 */
public enum MessageType {

    /**
     * When received by the server, it will respond with a list of available 
     * waiting rooms. When received by a client, they should decode the waiting
     * rooms listed within.
     */
    LIST_WAITING_ROOMS("list waiting rooms"),

    /**
     * When received by the server, it will respond with the port of a new 
     * waiting room. When received by a client, they should connect to the port
     * contained in the message.
     */
    NEW_WAITING_ROOM("new waiting room");

    private final String name;

    private MessageType(String name) {
        this.name = name;
    }

    public static MessageType fromString(String name) {
        MessageType fromString = Arrays.stream(values())
            .filter((type) -> type.toString().equals(name))
            .findFirst()
            .get(); // throws error if not found
        return fromString;
    }

    @Override
    public String toString() {
        return name;
    }
}
