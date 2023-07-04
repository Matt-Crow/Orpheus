package world.builds.passives;

import java.util.UUID;

import gui.graphics.CustomColors;
import world.builds.actives.Range;
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
        withUser(u -> u.getActionRegister().addOnUseMelee(this));
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
        var useId = UUID.randomUUID().hashCode();
        spawnProjectile(useId, e, -15);
        spawnProjectile(useId, e, 0);
        spawnProjectile(useId, e, 15);
    }

    private void spawnProjectile(int useId, OnUseMeleeEvent e, int offsetDegrees) {
        var p = new ProjectileBuilder()
            .withUseId(useId)
            .at(getUser())
            .facing(getUser().getFacing().rotatedBy(offsetDegrees))
            .withMomentum(15) // todo move medium or fast
            .withRange(Range.SHORT)
            .withUser(getUser())
            .withParticles(new ParticleGenerator(CustomColors.FIRE, ParticleType.SHEAR))
            .onCollide(new ProjectileInflictBehavior(new Burn(1, 5)))
            .build();
        getUser().spawn(p);
    }
}
