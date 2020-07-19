package net.protocols;

import net.OrpheusServer;
import net.ServerMessage;
import util.SerialUtil;
import world.RemoteProxyWorld;
import world.WorldContent;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends AbstractOrpheusServerProtocol{
    private final RemoteProxyWorld proxy;
    
    /** 
     * @param localProxy the RemoteProxyWorld shell created on the clients
     * computer. World updates received by this protocol will be applied to
     * that proxy.
     */
    public RemoteProxyWorldProtocol(RemoteProxyWorld localProxy){
        proxy = localProxy;
    }
    
    /**
     * Receives a world update from the host, then applies
     * it to the client's proxy.
     * 
     * @param sm 
     */
    private void receiveWorldUpdate(ServerMessage sm){
        WorldContent content = (WorldContent)SerialUtil.fromSerializedString(sm.getBody());
        proxy.setContent(content);
        /*
        Old receiveWorldUpdate, not sure if I need any of this
        
        //synchronized(teams){
        SwingUtilities.invokeLater(()->{
            Team[] ts = (Team[])SerialUtil.fromSerializedString(sm.getBody());
            setPlayerTeam(ts[0]);
            setEnemyTeam(ts[1]);

            Master.getUser().linkToRemotePlayerInWorld(this); //since teams have changed
        });
        
        //}
        */
    }
    
    @Override
    public void doApplyProtocol() {}
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean handled = true;
        switch(sm.getType()){
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
