package net.protocols;

import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;

/**
 * an AbstractProtocol is responsible for receiving messages sent to a server
 */
public abstract class AbstractProtocol {
    private final AbstractNetworkClient server;


    public AbstractProtocol(AbstractNetworkClient server){
        this.server = server;
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
