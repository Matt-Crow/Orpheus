package orpheus.core.net.protocols;

import java.util.HashMap;
import java.util.function.Consumer;

import orpheus.core.net.*;

/**
 * Handles messages from a remote server or client.
 */
public class MessageHandler {
    private final HashMap<ServerMessageType, Consumer<Message>> handlers = new HashMap<>();

    /**
     * Adds a handler for a specific message type.
     * @param type the type of message to handle
     * @param handler how to handle messages of the given type
     * @return this, for chaining
     */
    public final MessageHandler addHandler(ServerMessageType type, Consumer<Message> handler) {
        handlers.put(type, handler);
        return this;
    }

    /**
     * Adds a handler for a specific message type.
     * @param type the type of message to handle
     * @param handler how to handle messages of the given type
     * @return this, for chaining
     */
    public final MessageHandler addHandler(ServerMessageType type, Runnable handler) {
        return addHandler(type, ignored -> handler.run());
    }

    /**
     * @param message the message received by the server
     * @return true iff this handled the packet
     */
    public final boolean handleMessage(Message message) {
        var type = message.getType();
        if (handlers.containsKey(type)) {
            handlers
                .get(type)
                .accept(message);
            return true;
        }
        return false;
    }

    /**
     * Invoked by AbstractNetworkClient when it starts using this
     */
    public void handleStart() {

    }

    /**
     * Invoked by AbstractNetworkClient when it stops using this
     */
    public void handleStop() {

    }
}
