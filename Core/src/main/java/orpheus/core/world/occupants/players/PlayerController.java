package orpheus.core.world.occupants.players;

import orpheus.core.utils.timer.TimerTask;
import world.entities.AbstractPlayer;

/**
 * controls a player
 */
public interface PlayerController extends TimerTask {
    
    /**
     * @return the player this is controlling
     */
    public AbstractPlayer getControlled();

    public default boolean isDone() {
        return getControlled().isTerminating();
    }
}
