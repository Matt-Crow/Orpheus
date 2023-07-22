package world.builds.actives;

import gui.graphics.CustomColors;
import orpheus.core.world.occupants.players.attributes.requirements.NotInUseRequirement;
import util.Direction;
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
            10, // fast
            Range.NONE, 
            4 // moderate damage
        );
        setColors(CustomColors.METAL);
        setParticleType(ParticleType.BURST);
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
    public Projectile createProjectile(Direction facing) {
        var p = super.createProjectile(facing);
        p.addTerminationListener(t -> {
            if (t == p) {
                midair.doneUsing();
            }
        });
        return p;
    }

    @Override
    public String getDescription() {
        return "The user throws a mighty hammer at a distant enemy.";
    }
}
