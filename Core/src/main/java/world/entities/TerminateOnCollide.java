package world.entities;

import orpheus.core.world.occupants.players.Player;

public class TerminateOnCollide implements ProjectileCollideBehavior {

    @Override
    public void collidedWith(Projectile projectile, Player target) {
        projectile.terminate();
    }
}
