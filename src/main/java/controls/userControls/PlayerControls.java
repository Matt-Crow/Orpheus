package controls.userControls;

import commands.ControlPressed;
import start.AbstractOrpheusCommandInterpreter;
import world.AbstractWorldShell;

/**
 *
 * @author Matt Crow
 */
public class PlayerControls extends AbstractPlayerControls {
    private final AbstractOrpheusCommandInterpreter interpreter;
    public PlayerControls(AbstractWorldShell inWorld, String playerId, AbstractOrpheusCommandInterpreter interpreter) {
        super(inWorld, playerId);
        this.interpreter = interpreter;
    }

    @Override
    public void consumeCommand(String command) {
        interpreter.doSendMessage(new ControlPressed(this.getWorld(), command));
    }

}
