package controls.userControls;

import java.net.InetAddress;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
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
    public void consumeCommand(String command) {
        OrpheusServer.getInstance().send(new ServerMessage(
            command,
            ServerMessageType.CONTROL_PRESSED
        ),
            receiverIpAddr
        );
    }
}
