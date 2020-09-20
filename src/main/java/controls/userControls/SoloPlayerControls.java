package controls.userControls;

import world.AbstractWorldShell;

/**
 *
 * @author Matt
 */
public class SoloPlayerControls extends AbstractPlayerControls{

    public SoloPlayerControls(AbstractWorldShell inWorld, String playerId) {
        super(inWorld, playerId);
    }

    @Override
    public void consumeCommand(String command) {
        AbstractPlayerControls.decode(getWorld(), command);
    }
}
