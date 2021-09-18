package controls.userControls;

import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import users.AbstractUser;
import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final AbstractUser host;
    
    public RemotePlayerControls(AbstractWorldShell inWorld, String playerId, AbstractUser host){
        super(inWorld, playerId);
        this.host = host;
    }

    @Override
    public void consumeCommand(String command) {
        OrpheusServer.getInstance().send(new ServerMessage(
            command,
            ServerMessageType.CONTROL_PRESSED
        ),
            host
        );
    }
}
