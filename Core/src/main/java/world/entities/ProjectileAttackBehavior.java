package world.entities;

import world.builds.actives.ElementalActive;

/**
 * Specifies that a projectile should resolve an attack upon colliding with a
 * player.
 */
public class ProjectileAttackBehavior implements ProjectileCollideBehavior {

    private final ElementalActive active;

    protected ProjectileAttackBehavior(ElementalActive active) {
        this.active = active;
    }

    @Override
    public void collidedWith(Projectile projectile, AbstractPlayer target) {
        active.hit(projectile, target);
    }
}
