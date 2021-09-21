package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessagePacket;

/**
 *
 * @author Matt
 */
public abstract class AbstractOrpheusServerProtocol {
    private final OrpheusServer runningServer;
    
    protected AbstractOrpheusServerProtocol(OrpheusServer runningServer){
        this.runningServer = runningServer;
    }
    
    public final OrpheusServer getServer(){
        return runningServer;
    }
    
    /**
     * An instance of OrpheusServer should pass itself as a parameter to this function
     * upon receiving a server message.
     * 
     * @param sm the message received
     * @param forServer the server which received this message
     * @return whether or not this method handled the message.
     */
    public abstract boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer);
}
