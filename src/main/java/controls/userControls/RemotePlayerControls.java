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
    private final OrpheusServer server;
    
    public RemotePlayerControls(OrpheusServer server, AbstractWorldShell inWorld, String playerId, AbstractUser host){
        super(inWorld, playerId);
        this.host = host;
        this.server = server;
    }

    @Override
    public void consumeCommand(String command) {
        server.send(new ServerMessage(
            command,
            ServerMessageType.CONTROL_PRESSED
        ),
            host
        );
    }
}
