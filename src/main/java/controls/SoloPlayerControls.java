package controls;

import world.AbstractWorld;

/**
 *
 * @author Matt
 */
public class SoloPlayerControls extends AbstractPlayerControls{

    public SoloPlayerControls(AbstractWorld inWorld, String playerId) {
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
