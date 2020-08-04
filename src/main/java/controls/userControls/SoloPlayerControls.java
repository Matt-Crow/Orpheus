package controls.userControls;

import controls.userControls.AbstractPlayerControls;
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
    public void useMeleeKey() {
        AbstractPlayerControls.decode(getWorld(), meleeString());
    }

    @Override
    public void useAttKey(int i) {
        AbstractPlayerControls.decode(getWorld(), attString(i));
    }

    @Override
    public void move() {
        AbstractPlayerControls.decode(getWorld(), moveString());
    }

}
