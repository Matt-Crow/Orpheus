package orpheus.core.world.occupants.players;

import orpheus.core.utils.timer.TimerTask;

/**
 * controls a player
 */
public interface PlayerController extends TimerTask {
    
    /**
     * @return the player this is controlling
     */
    public Player getControlled();

    public default boolean isDone() {
        return getControlled().isTerminating();
    }
}
