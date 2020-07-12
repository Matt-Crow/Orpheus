package controls;

import controllers.Master;
import entities.HumanPlayer;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final String receiverIpAddr;
    
    public RemotePlayerControls(HumanPlayer forPlayer, String receiverIp){
        super(forPlayer);
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
