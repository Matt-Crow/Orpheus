package world.entities;

import util.Direction;
import util.Settings;
import world.builds.actives.Range;
import world.builds.actives.Speed;

public class Explodes {
    
    /**
     * the radius of the explosion this generates
     */
    private final Range areaOfEffect;

    /**
     * how fast the explosion spreads
     */
    private final Speed speed;

    /**
     * what should happen when the explosion hits an enemy
     */
    private final ProjectileCollideBehavior collideBehavior;

    /**
     * whether this has already exploded, and thus should not explode again
     */
    private boolean exploded = false;

    public Explodes(Range areaOfEffect, Speed speed, ProjectileCollideBehavior behavior) {
        this.areaOfEffect = areaOfEffect;
        this.speed = speed;
        this.collideBehavior = behavior;
    }

    /**
     * Explodes from the given projectile, if this has not yet exploded.
     * @param projectile the projectile this exploded from
     */
    protected synchronized void explode(Projectile projectile) {
        if (!exploded && areaOfEffect != Range.NONE) {
            doExplode(projectile);
        }
    }

    private void doExplode(Projectile projectile) {       
        var user = projectile.getSpawner();
        var builder = new ProjectileBuilder()
            .withUser(user)
            .at(projectile)
            .withParticles(projectile.getParticleGenerator())
            .havingHitThusFar(projectile.getPlayersHitThusFar())
            .withRange(areaOfEffect)
            .withMomentum(speed)
            .onCollide(collideBehavior);
        
        var arc = 360.0 / Settings.TICKSTOROTATE;
        for (int i = 0; i < Settings.TICKSTOROTATE; i++) {
            user.spawn(builder.facing(Direction.fromDegrees((int)(arc*i))).build());
        }
        exploded = true;
    }
}
