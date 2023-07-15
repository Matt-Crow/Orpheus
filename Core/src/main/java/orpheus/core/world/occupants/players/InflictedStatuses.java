package orpheus.core.world.occupants.players;

import java.util.HashMap;
import java.util.List;

import world.statuses.AbstractStatus;
import world.statuses.StatusName;

/**
 * Keeps track of the statuses inflicted upon a player
 */
public class InflictedStatuses {
    
    /**
     * maps status names to a status - only one instance of each status may be
     * inflicted at one time
     */
    private final HashMap<StatusName, AbstractStatus> nameToStatus;

    /**
     * the player this is attached to
     */
    private final Player player;

    protected InflictedStatuses(Player player) {
        nameToStatus = new HashMap<>();
        this.player = player;
    }

    /**
     * removes all statuses
     */
    protected void clear() {
        nameToStatus.clear();
    }

    /**
     * inflicts the player with the given status, if they are not already 
     * inflicted with a higher level or one of equal level but more remaining
     * users.
     */
    protected void add(AbstractStatus newStatus) {
        var key = newStatus.getStatusName();
        var oldStatusOrNull = nameToStatus.get(key);
        if (newStatus.isBetterThan(oldStatusOrNull)) {
            nameToStatus.put(key, newStatus);
            newStatus.inflictOn(player);
            newStatus.addTerminationListener(e -> {
                // check if still inflicted
                if (newStatus == nameToStatus.get(key)) {
                    nameToStatus.remove(key);
                }
            });
        }
    }

    /**
     * @return all statuses the player is currently inflicted with
     */
    protected List<AbstractStatus> toList() {
        return List.copyOf(nameToStatus.values());
    }
}
