package net.protocols;

import java.util.HashMap;
import java.util.function.Consumer;

import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;

/**
 * Handles messages from a remote server or client.
 */
public class MessageHandler {

    private final AbstractNetworkClient server;
    private final HashMap<ServerMessageType, Consumer<ServerMessagePacket>> handlers = new HashMap<>();

    public MessageHandler(AbstractNetworkClient runningServer) {
        server = runningServer;
    }

    protected AbstractNetworkClient getServer(){
        return server;
    }

    /**
     * Adds a handler for a specific message type.
     * @param type the type of message to handle
     * @param handler how to handle messages of the given type
     * @return this, for chaining
     */
    public final MessageHandler addHandler(ServerMessageType type, Consumer<ServerMessagePacket> handler) {
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
     * @param smp the message received by the server
     * @return true iff this handled the packet
     */
    public final boolean handleMessage(ServerMessagePacket smp) {
        var type = smp.getMessage().getType();
        if (handlers.containsKey(type)) {
            handlers
                .get(type)
                .accept(smp);
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
