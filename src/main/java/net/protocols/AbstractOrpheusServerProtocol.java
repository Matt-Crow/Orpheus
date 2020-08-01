package net.protocols;

import java.io.IOException;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;

/**
 *
 * @author Matt
 */
public abstract class AbstractOrpheusServerProtocol {
    public abstract void applyProtocol() throws IOException;
    
    /**
     * An instance of OrpheusServer should pass itself as a parameter to this function
     * upon receiving a server message.
     * 
     * @param sm the message received
     * @param forServer the server which received this message
     * @return whether or not this method handled the message.
     */
    public abstract boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer);
    
    /**
     * Called whenever applyProtocol() is
     * invoked. This should reset the protocol,
     * preparing it.
     */
    public abstract void doApplyProtocol();
}
