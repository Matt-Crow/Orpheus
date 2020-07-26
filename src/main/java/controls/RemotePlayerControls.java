package controls;

import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final String receiverIpAddr;
    
    public RemotePlayerControls(AbstractWorldShell inWorld, String playerId, String receiverIp){
        super(inWorld, playerId);
        receiverIpAddr = receiverIp;
    }
    
    @Override
    public final void useMeleeKey(){
        OrpheusServer.getInstance().send(
            new ServerMessage(
                meleeString(),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }

    @Override
    public void useAttKey(int i) {
        OrpheusServer.getInstance().send(
            new ServerMessage(
                attString(i),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }

    @Override
    public void move() {
        OrpheusServer.getInstance().send(
            new ServerMessage(
                moveString(),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }
}
