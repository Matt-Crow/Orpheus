package world.entities;

import util.Settings;
import world.builds.actives.ElementalActive;
import world.builds.actives.Range;

public class Explodes {
    
    private final ElementalActive active;
    private boolean exploded = false;

    public Explodes(ElementalActive active) {
        this.active = active;
    }

    protected synchronized void explode(Projectile projectile) {
        if (!exploded) {
            doExplode(projectile);
        }
    }

    private void doExplode(Projectile projectile) {
        if (active.getAOE() == Range.NONE) {
            return;
        }
        
        var user = active.getUser();
        var world = user.getWorld();
        for (int i = 0; i < Settings.TICKSTOROTATE; i++) {
            user.spawn(Projectile.explosion(
                world,
                projectile.getUseId(),
                projectile.getX(),
                projectile.getY(),
                360 * i / Settings.TICKSTOROTATE,
                5,
                active
            ));
        }
        exploded = true;
    }
}
