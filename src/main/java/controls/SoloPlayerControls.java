package controls;

import entities.HumanPlayer;
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
        AbstractPlayerControls.decode((HumanPlayer)getPlayer(), meleeString());
    }

    @Override
    public void useAttKey(int i) {
        AbstractPlayerControls.decode((HumanPlayer)getPlayer(), attString(i));
    }

    @Override
    public void move() {
        AbstractPlayerControls.decode((HumanPlayer)getPlayer(), moveString());
    }

}
