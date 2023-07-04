package world.builds.actives;

import java.util.ArrayList;
import java.util.List;

import gui.graphics.CustomColors;
import util.Direction;
import world.entities.ParticleType;
import world.entities.Projectile;
import world.statuses.Stun;

public class HammerToss extends ElementalActive {

    /**
     * whether the hammer is in mid-air, and thus is unavailable
     */
    private boolean traveling;

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
    }
    
    @Override
    public HammerToss copy() {
        return new HammerToss();
    }

    @Override
    public void init() {
        super.init();
        traveling = false;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !traveling;
    }

    @Override
    protected void doUse() {
        super.doUse();
        traveling = true;
    }

    @Override
    public Projectile createProjectile(Direction facing) {
        var p = super.createProjectile(facing);
        p.addTerminationListener(t -> {
            if (t == p) {
                traveling = false;
            }
        });
        return p;
    }

    @Override
    public String getDescription() {
        return "The user throws a mighty hammer at a distant enemy.";
    }

    @Override
    protected List<String> getUnavailabilityMessages() {
        var result = new ArrayList<>(super.getUnavailabilityMessages());
        if (traveling) {
            result.add("In midair");
        }
        return result;
    }
}
