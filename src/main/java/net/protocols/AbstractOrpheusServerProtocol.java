package net.protocols;

import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;

/**
 *
 * @author Matt
 * @param <T> the type of network client that will use this protocol
 */
public abstract class AbstractOrpheusServerProtocol<T extends AbstractNetworkClient> {
    private final T runningServer;
    
    protected AbstractOrpheusServerProtocol(T runningServer){
        this.runningServer = runningServer;
    }
    
    public final T getServer(){
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
    public abstract boolean receiveMessage(ServerMessagePacket sm, T forServer);
}
