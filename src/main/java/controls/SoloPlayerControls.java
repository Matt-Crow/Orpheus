package controls;

import entities.HumanPlayer;

/**
 *
 * @author Matt
 */
public class SoloPlayerControls extends AbstractPlayerControls{

    public SoloPlayerControls(HumanPlayer forPlayer) {
        super(forPlayer);
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
