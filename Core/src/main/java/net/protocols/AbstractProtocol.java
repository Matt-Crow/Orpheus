package net.protocols;

import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;

/**
 * Runs on an AbstractNetworkClient and dictates how it should react to messages
 * received.
 * 
 * @author Matt Crow
 */
public abstract class AbstractProtocol {
    private final AbstractNetworkClient server;

    public AbstractProtocol(AbstractNetworkClient runningServer) {
        this.server = runningServer;
    }

    protected AbstractNetworkClient getServer(){
        return server;
    }

    /**
     * @param smp the message received by the server
     * @return true iff this handled the packet
     */
    public abstract boolean receive(ServerMessagePacket smp);
}
