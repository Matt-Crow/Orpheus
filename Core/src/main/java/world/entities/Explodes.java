package world.entities;

import util.Direction;
import util.Settings;
import world.builds.actives.ElementalActive;
import world.builds.actives.Range;
import world.builds.actives.Speed;

public class Explodes {
    
    private final ElementalActive active;
    private boolean exploded = false;

    public Explodes(ElementalActive active) {
        this.active = active;
    }

    protected synchronized void explode(Projectile projectile) {
        if (!exploded && active.getAOE() != Range.NONE) {
            doExplode(projectile);
        }
    }

    private void doExplode(Projectile projectile) {       
        var user = active.getUser();
        var builder = new ProjectileBuilder()
            .exploded(active)
            .at(projectile)
            .havingHitThusFar(projectile.getPlayersHitThusFar())
            .withMomentum(Speed.MEDIUM);
        for (int i = 0; i < Settings.TICKSTOROTATE; i++) {
            user.spawn(builder.facing(Direction.fromDegrees(360 * i / Settings.TICKSTOROTATE)).build());
        }
        exploded = true;
    }
}
