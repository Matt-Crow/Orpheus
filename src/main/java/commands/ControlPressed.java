package commands;

import controls.userControls.AbstractPlayerControls;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import world.AbstractWorldShell;

/**
 *
 * @author Matt Crow
 */
public class ControlPressed implements SerializeableOrpheusCommand {
    private final AbstractWorldShell world;
    private final String controlString;
    
    public ControlPressed(AbstractWorldShell world, String controlString){
        this.world = world;
        this.controlString = controlString;
    }
    
    @Override
    public ServerMessage serialize() {
        ServerMessage sm = new ServerMessage(controlString, ServerMessageType.CONTROL_PRESSED);
        return sm;
    }

    @Override
    public void execute() {
        AbstractPlayerControls.decode(world, controlString);
    }
}
