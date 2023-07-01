package world.entities;

/**
 * specifies how projectiles should react to colliding with a player
 */
public interface ProjectileCollideBehavior {
    
    /**
     * Determines what to do upon colliding with the given player.
     * @param projectile the projectile which collided
     * @param target the player with whom this collided.
     */
    public void collidedWith(Projectile projectile, AbstractPlayer target);
}
