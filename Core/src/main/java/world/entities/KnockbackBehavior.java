package world.entities;

import orpheus.core.world.occupants.players.Player;
import util.Settings;
import world.builds.actives.Range;

public class KnockbackBehavior implements ProjectileCollideBehavior {

    /**
     * how far the target is knocked back
     */
    private final Range distance;


    public KnockbackBehavior(Range distance) {
        this.distance = distance;
    }
    

    @Override
    public void collidedWith(Projectile projectile, Player target) {
        target.knockBack(
            distance.getInPixels(), 
            projectile.getFacing(), 
            Settings.seconds(1)
        );    
    }
}
