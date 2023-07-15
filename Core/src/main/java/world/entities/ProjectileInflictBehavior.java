package world.entities;

import orpheus.core.world.occupants.players.Player;
import world.statuses.AbstractStatus;

public class ProjectileInflictBehavior implements ProjectileCollideBehavior {

    private final AbstractStatus inflicts;

    public ProjectileInflictBehavior(AbstractStatus inflicts) {
        this.inflicts = inflicts;
    }

    @Override
    public void collidedWith(Projectile projectile, Player target) {
        target.inflict(inflicts.copy());
    }
    
}
