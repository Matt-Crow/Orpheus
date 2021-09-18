package controls.userControls;

import java.net.Socket;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final Socket receiverIpAddr;
    
    public RemotePlayerControls(AbstractWorldShell inWorld, String playerId, Socket receiverIp){
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
