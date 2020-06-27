package controls;

import controllers.Master;
import entities.HumanPlayer;
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
        Master.SERVER.send(
            new ServerMessage(
                meleeString(),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }

    @Override
    public void useAttKey(int i) {
        Master.SERVER.send(
            new ServerMessage(
                attString(i),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }

    @Override
    public void move() {
        Master.SERVER.send(
            new ServerMessage(
                moveString(),
                ServerMessageType.CONTROL_PRESSED
            ), 
            receiverIpAddr
        );
    }
}
