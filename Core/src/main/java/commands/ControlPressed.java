package commands;

import commands.ControlDecoder;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import world.World;

/**
 *
 * @author Matt Crow
 */
public class ControlPressed implements SerializeableOrpheusCommand {
    private final World world;
    private final String controlString;
    
    public ControlPressed(World world, String controlString){
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
        ControlDecoder.decode(world, controlString);
    }
}
