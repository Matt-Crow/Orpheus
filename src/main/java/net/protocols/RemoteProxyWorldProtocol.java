package net.protocols;

import net.OrpheusClient;
import net.messages.ServerMessagePacket;
import serialization.WorldSerializer;
import world.World;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends AbstractOrpheusServerNonChatProtocol{
    private final WorldSerializer serializer;
    
    /**
     * @param runningServer
     * @param localProxy the RemoteProxyWorld shell created on the clients
     * computer. World updates received by this protocol will be applied to
     * that proxy.
     */
    public RemoteProxyWorldProtocol(OrpheusClient runningServer, World localProxy){
        super(runningServer);
        serializer = new WorldSerializer(localProxy);
    }
    
    /**
     * Receives a world update from the host, then applies
     * it to the client's proxy.
     * 
     * @param sm 
     */
    private void receiveWorldUpdate(ServerMessagePacket sm){
        serializer.deserialize(sm.getMessage().getBody());       
    }
    
    @Override
    public boolean receive(ServerMessagePacket sm) {
        boolean handled = true;
        switch(sm.getMessage().getType()){
            case WORLD_UPDATE:
                receiveWorldUpdate(sm);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
}
