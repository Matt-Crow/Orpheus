package controls.userControls;

import net.OrpheusClient;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class RemotePlayerControls extends AbstractPlayerControls{
    private final OrpheusClient server;
    
    public RemotePlayerControls(OrpheusClient server, AbstractWorldShell inWorld, String playerId){
        super(inWorld, playerId);
        this.server = server;
    }

    @Override
    public void consumeCommand(String command) {
        server.send(new ServerMessage(
            command,
            ServerMessageType.CONTROL_PRESSED
        ));
    }
}
