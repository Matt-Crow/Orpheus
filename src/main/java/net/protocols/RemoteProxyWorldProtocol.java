package net.protocols;

import net.OrpheusClient;
import net.messages.ServerMessagePacket;
import util.SerialUtil;
import world.RemoteProxyWorld;
import world.WorldContent;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends AbstractOrpheusServerNonChatProtocol<OrpheusClient>{
    private final RemoteProxyWorld proxy;
    
    /**
     * @param runningServer
     * @param localProxy the RemoteProxyWorld shell created on the clients
     * computer. World updates received by this protocol will be applied to
     * that proxy.
     */
    public RemoteProxyWorldProtocol(OrpheusClient runningServer, RemoteProxyWorld localProxy){
        super(runningServer);
        proxy = localProxy;
    }
    
    /**
     * Receives a world update from the host, then applies
     * it to the client's proxy.
     * 
     * @param sm 
     */
    private void receiveWorldUpdate(ServerMessagePacket sm){
        WorldContent content = (WorldContent)SerialUtil.fromSerializedString(sm.getMessage().getBody());
        proxy.setContent(content);
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusClient forServer) {
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
