package world.builds.actives;

import world.entities.KnockbackBehavior;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.ProjectileBuilder;
import gui.graphics.CustomColors;

/**
 *
 * @author Matt Crow
 */
public class BoulderToss extends ElementalActive{
    public BoulderToss(){
        super(
            "Boulder Toss", 
            Arc.NONE, 
            Range.SHORT, 
            Speed.SLOW, 
            Range.MEDIUM, 
            Damage.HIGH, 
            new ParticleGenerator(CustomColors.EARTH, ParticleType.BURST)
        );
    }
    

    @Override
    protected ProjectileBuilder withProjectileBuilder(ProjectileBuilder builder) {
        return builder.onCollide(new KnockbackBehavior(getRange()));
    }
    
    @Override
    public BoulderToss copy(){
        return new BoulderToss();
    }
}
