package world.events;

import orpheus.core.world.occupants.WorldOccupant;
import world.builds.actives.MeleeActive;

/**
 * fired whenever a WorldOccupant makes a melee attack
 */
public class OnUseMeleeEvent {
    
    private final WorldOccupant user;
    private final MeleeActive used;

    public OnUseMeleeEvent(WorldOccupant user, MeleeActive used) {
        this.user = user;
        this.used = used;
    }

    /**
     * @return the WorldOccupant who used a melee active
     */
    public WorldOccupant getUser() {
        return user;
    }

    /**
     * @return the melee active which was used
     */
    public MeleeActive getUsed() {
        return used;
    }
}
