package controls.userControls;

import java.net.InetAddress;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import util.CardinalDirection;
import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final InetAddress receiverIpAddr;
    
    public RemotePlayerControls(AbstractWorldShell inWorld, String playerId, InetAddress receiverIp){
        super(inWorld, playerId);
        receiverIpAddr = receiverIp;
    }
    
    @Override
    public final void useMeleeKey(){
        OrpheusServer.getInstance().send(new ServerMessage(
            meleeString(),
            ServerMessageType.CONTROL_PRESSED
        ),
            receiverIpAddr
        );
    }

    @Override
    public void useAttKey(int i) {
        OrpheusServer.getInstance().send(new ServerMessage(
            attString(i),
            ServerMessageType.CONTROL_PRESSED
        ),
            receiverIpAddr
        );
    }

    @Override
    public void move() {
        OrpheusServer.getInstance().send(new ServerMessage(
            moveString(),
            ServerMessageType.CONTROL_PRESSED
        ),
            receiverIpAddr
        );
    }

    @Override
    public void useDirKey(CardinalDirection d) {
        OrpheusServer.getInstance().send(new ServerMessage(
            directionString(d),
            ServerMessageType.CONTROL_PRESSED
        ),
            receiverIpAddr
        );}
}
