package world.builds.actives;

import gui.graphics.CustomColors;
import orpheus.core.world.occupants.players.attributes.requirements.NotInUseRequirement;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.Projectile;
import world.statuses.Stun;

public class HammerToss extends ElementalActive {

    private final NotInUseRequirement midair;

    public HammerToss() {
        super(
            "Hammer Toss", 
            Arc.NONE, 
            Range.LONG, 
            Speed.FAST,
            Range.NONE, 
            Damage.MEDIUM,
            new ParticleGenerator(CustomColors.METAL, ParticleType.BURST)
        );
        addStatus(new Stun(1, 3));
        andProjectilesTerminateOnHit();

        midair = new NotInUseRequirement("In midair");
        andRequires(midair);
    }
    
    @Override
    public HammerToss copy() {
        return new HammerToss();
    }

    @Override
    public void init() {
        super.init();
        midair.doneUsing();
    }

    @Override
    protected void doUse() {
        super.doUse();
        midair.use();
    }

    @Override
    public void modifyProjectile(Projectile projectile) {
        projectile.addTerminationListener(t -> {
            if (t == projectile) {
                midair.doneUsing();
            }
        });
    }

    @Override
    public String getDescription() {
        return "The user throws a mighty hammer at a distant enemy.";
    }
}
