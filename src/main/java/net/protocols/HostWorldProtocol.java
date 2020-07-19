package net.protocols;

import controls.AbstractPlayerControls;
import entities.HumanPlayer;
import net.OrpheusServer;
import net.ServerMessage;
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
public class HostWorldProtocol extends AbstractOrpheusServerProtocol{
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
    
    private void receiveControl(ServerMessage sm){
        throw new RuntimeException("Need to redo how controls work!");
        // extract HumanPlayer ID from sm
        // find their player in the WorldContent (if they are alive)
        // apply the control to that player
        
        
        //HumanPlayer p = sm.getSender().getPlayer();
        //AbstractPlayerControls.decode(p, sm.getBody());
    }
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean handled = true;
        switch(sm.getType()){
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
