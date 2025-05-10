package world.builds.passives;

import java.util.HashSet;

import gui.graphics.CustomColors;
import orpheus.core.world.occupants.players.Player;
import world.builds.actives.Range;
import world.builds.actives.Speed;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.ProjectileBuilder;
import world.entities.ProjectileInflictBehavior;
import world.events.EventListener;
import world.events.OnUseMeleeEvent;
import world.statuses.Burn;

public class CinderStrikes extends AbstractPassive implements EventListener<OnUseMeleeEvent> {

    public CinderStrikes() {
        super("Cinder Strikes", false);
    }

    @Override
    public void init() {
        withUser(u -> u.eventOnUseMelee().add(this));
    }

    @Override
    public CinderStrikes copy() {
        return new CinderStrikes();
    }

    @Override
    public String getDescription() {
        return "The user's melee attacks launch cinders that burn enemies.";
    }

    @Override
    public void handle(OnUseMeleeEvent e) {
        var hitThusFar = new HashSet<Player>();
        spawnProjectile(hitThusFar, e, -15);
        spawnProjectile(hitThusFar, e, 0);
        spawnProjectile(hitThusFar, e, 15);
    }

    private void spawnProjectile(HashSet<Player> hitThusFar, OnUseMeleeEvent e, int offsetDegrees) {
        var p = new ProjectileBuilder()
            .havingHitThusFar(hitThusFar)
            .at(getUser())
            .facing(getUser().getFacing().rotatedBy(offsetDegrees))
            .withMomentum(Speed.MEDIUM)
            .withRange(Range.SHORT)
            .withUser(getUser())
            .withParticles(new ParticleGenerator(CustomColors.FIRE, ParticleType.SHEAR))
            .onCollide(new ProjectileInflictBehavior(new Burn(1, 5)))
            .build();
        getUser().spawn(p);
    }
}
