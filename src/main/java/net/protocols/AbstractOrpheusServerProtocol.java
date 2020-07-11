package net.protocols;

import net.OrpheusServer;
import net.ServerMessage;

/**
 * This class should be used to clarify exactly
 * what the OrpheusServer should do with messages
 * at any given time.
 * 
 * @author Matt Crow
 */
public interface AbstractOrpheusServerProtocol {
    
    /**
     * An instance of OrpheusServer should pass itself as a parameter to this function
     * upon receiving a server message.
     * 
     * @param sm the message received
     * @param forServer the server which received this message
     * @return whether or not this method handled the message.
     */
    public abstract boolean receiveMessage(ServerMessage sm, OrpheusServer forServer);
}
