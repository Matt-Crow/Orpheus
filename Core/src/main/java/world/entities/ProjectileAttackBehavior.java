package world.entities;

import orpheus.core.world.occupants.players.Player;
import world.builds.actives.Attack;

/**
 * Specifies that a projectile should resolve an attack upon colliding with a
 * player.
 */
public class ProjectileAttackBehavior implements ProjectileCollideBehavior {

    private final Attack attack;

    public ProjectileAttackBehavior(Attack attack) {
        this.attack = attack;
    }

    @Override
    public void collidedWith(Projectile projectile, Player target) {
        attack.resolveAgainst(target);
    }
}
