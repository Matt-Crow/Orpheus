package net.protocols;

import controls.userControls.AbstractPlayerControls;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import world.HostWorld;

/**
 * The HostWorldProtocol is used by hosts to receive remote player controls,
 * applying the controls to players in the host's world as though the 
 * client pressed keys on the host's computer.
 * 
 * This will need to change once I update how remote controls work
 * 
 * @author Matt Crow
 */
public class HostWorldProtocol extends AbstractOrpheusServerNonChatProtocol{
    private final HostWorld hostWorld;
    
    /**
     * 
     * @param forWorld the world  
     */
    public HostWorldProtocol(HostWorld forWorld){
        hostWorld = forWorld;
    }
    
    @Override
    public void doApplyProtocol() {}
    
    private void receiveControl(ServerMessagePacket sm){
        // extract HumanPlayer ID from sm
        //String id = sm.getSender().getRemotePlayerId(); // is this set on the host's side?
        // find their player in the WorldContent (if they are alive)
        //AbstractPlayer player = hostWorld.getPlayerTeam().getMemberById(id);
        // apply the control to that player
        //if(player != null && player instanceof HumanPlayer){
            //AbstractPlayerControls.decode((HumanPlayer) player, sm.getBody());
        //}
        AbstractPlayerControls.decode(hostWorld, sm.getMessage().getBody());
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        boolean handled = true;
        switch(sm.getMessage().getType()){
            case CONTROL_PRESSED:
                receiveControl(sm);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
}
