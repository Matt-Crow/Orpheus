package controls;

import entities.HumanPlayer;
import world.AbstractWorld;

/**
 *
 * @author Matt
 */
public class SoloPlayerControls extends AbstractPlayerControls{

    public SoloPlayerControls(HumanPlayer forPlayer, AbstractWorld inWorld) {
        super(forPlayer, inWorld);
    }

    @Override
    public void useMeleeKey() {
        AbstractPlayerControls.decode(getPlayer(), meleeString());
    }

    @Override
    public void useAttKey(int i) {
        AbstractPlayerControls.decode(getPlayer(), attString(i));
    }

    @Override
    public void move() {
        AbstractPlayerControls.decode(getPlayer(), moveString());
    }

}
