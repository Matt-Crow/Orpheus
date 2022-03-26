package net.protocols;

import controls.PlayerControls;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import world.World;

/**
 * The HostWorldProtocol is used by hosts to receive remote player controls,
 * applying the controls to players in the host's world as though the 
 * client pressed keys on the host's computer.
 * 
 * Updating the world is currently handled by HostWorldUpdater
 * 
 * @author Matt Crow
 */
public class HostWorldProtocol extends AbstractOrpheusServerNonChatProtocol<OrpheusServer>{
    private final World hostWorld;
    
    public HostWorldProtocol(OrpheusServer runningServer, World forWorld){
        super(runningServer);
        hostWorld = forWorld;
    }
    
    private void receiveControl(ServerMessagePacket sm){
        PlayerControls.decode(hostWorld, sm.getMessage().getBody());
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        /*
        For some reason I cannot fathom, this often fails to receive messages containing
        remote user's clicking to move. I've checked, and the remote user is sending the
        messages, and this always handles the clicks when this receives the message, but
        the server doesn't always receive those messages. Odd how that only happens with
        movement.
        */
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
