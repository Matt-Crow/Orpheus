package commands;

import controls.PlayerControls;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import world.TempWorld;

/**
 *
 * @author Matt Crow
 */
public class ControlPressed implements SerializeableOrpheusCommand {
    private final TempWorld world;
    private final String controlString;
    
    public ControlPressed(TempWorld world, String controlString){
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
        PlayerControls.decode(world, controlString);
    }
}
