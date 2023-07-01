package world.entities;

import world.statuses.AbstractStatus;

public class ProjectileInflictBehavior implements ProjectileCollideBehavior {

    private final AbstractStatus inflicts;

    public ProjectileInflictBehavior(AbstractStatus inflicts) {
        this.inflicts = inflicts;
    }

    @Override
    public void collidedWith(Projectile projectile, AbstractPlayer target) {
        target.inflict(inflicts.copy());
    }
    
}
