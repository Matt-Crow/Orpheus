package commands;

import net.messages.ServerMessageType;
import orpheus.core.net.messages.Message;
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
    public Message serialize() {
        Message sm = new Message(controlString, ServerMessageType.CONTROL_PRESSED);
        return sm;
    }

    @Override
    public void execute() {
        ControlDecoder.decode(world, controlString);
    }
}
