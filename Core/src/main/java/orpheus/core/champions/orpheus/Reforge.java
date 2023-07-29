package orpheus.core.champions.orpheus;

import gui.graphics.CustomColors;
import orpheus.core.utils.coordinates.RotatingTerminalPointUpdater;
import util.Settings;
import world.Tile;
import world.builds.actives.AbstractActive;
import world.builds.actives.Arc;
import world.builds.actives.Attack;
import world.builds.actives.Damage;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.Projectile;
import world.entities.ProjectileAttackBehavior;
import world.entities.ProjectileBuilder;
import world.statuses.Resistance;
import world.statuses.Strength;

public class Reforge extends AbstractActive {

    private final OrpheusChampion orpheus;

    public Reforge(OrpheusChampion orpheus) {
        super(
            "Reforge", 
            new NeedScrapMetalRequirement(orpheus)
        );
        this.orpheus = orpheus;
    }

    @Override
    public Reforge copy() {
        return new Reforge(orpheus);
    }

    @Override
    protected void use() {
        var scrapMetal = orpheus.getScrapMetal();
        withUser((user) -> {
            var attack = new Attack(Damage.percent(0.075 * scrapMetal));
            var projectileBuilder = new ProjectileBuilder()
                .withUser(user)
                .at(user)
                .havingHitNoPlayersYet()
                .withParticles(new ParticleGenerator(CustomColors.METAL, ParticleType.BURST))
                .onCollide(new ProjectileAttackBehavior(attack));

            for (var i = 0; i < scrapMetal; i++) {
                user.spawn(spawnProjectile(projectileBuilder, i));
            }
            user.inflict(new Strength(scrapMetal, scrapMetal));
            user.inflict(new Resistance(scrapMetal, scrapMetal));
        });
        orpheus.clearScrapMetal();
    }

    private Projectile spawnProjectile(ProjectileBuilder projectileBuilder, int number) {
        var arc = Arc.WIDE;
        var p = projectileBuilder
            .withMovement(new RotatingTerminalPointUpdater(
                (number + 1) * Tile.TILE_SIZE, 
                getUser().getFacing().rotatedBy(-arc.getDegrees() / 2), 
                arc, 
                Settings.seconds(1), 
                () -> getUser().getCoordinates()
            ))
            .build();
        return p;
    }

    @Override
    public String getDescription() {
        return "Consumes all scrap metal to perform a powerful melee attack, which is more powerful the more scrap metal it consumes";
    }
}
