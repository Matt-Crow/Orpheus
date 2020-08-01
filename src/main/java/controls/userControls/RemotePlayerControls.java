package controls.userControls;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
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
        try {
            OrpheusServer.getInstance().send(
                new ServerMessage(
                    meleeString(),
                    ServerMessageType.CONTROL_PRESSED
                ),
                receiverIpAddr
            );
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void useAttKey(int i) {
        try {
            OrpheusServer.getInstance().send(
                new ServerMessage(
                    attString(i),
                    ServerMessageType.CONTROL_PRESSED
                ),
                receiverIpAddr
            );
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void move() {
        try {
            OrpheusServer.getInstance().send(
                new ServerMessage(
                    moveString(),
                    ServerMessageType.CONTROL_PRESSED
                ),
                receiverIpAddr
            );
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
    }
}
