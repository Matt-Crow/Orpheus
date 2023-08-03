package world.builds.actives;

import gui.graphics.CustomColors;
import orpheus.core.utils.coordinates.RotatingTerminalPointUpdater;
import util.Settings;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.ProjectileBuilder;

public class SweepingStrike extends MeleeActive {

    public SweepingStrike() {
        super("Sweeping Strike", Arc.NONE, Speed.FAST, Range.NONE, Damage.MEDIUM, new ParticleGenerator(CustomColors.METAL, ParticleType.BURST));
    }

    @Override
    public ProjectileBuilder withProjectileBuilder(ProjectileBuilder builder) {
        var arc = Arc.WIDE;
        var startingAngle = getUser().getFacing().rotatedBy(-arc.getDegrees() / 2);
        return builder.withMovement(new RotatingTerminalPointUpdater(
            Range.MELEE.getInPixels(), 
            startingAngle,
            arc, 
            Settings.seconds(0.5),
            () -> getUser().getCoordinates()
        ));
    }
    
    @Override
    public SweepingStrike copy() {
        return new SweepingStrike();
    }
}
