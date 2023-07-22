package world.builds.actives;

import util.Settings;
import world.entities.ParticleGenerator;
import world.entities.ParticleType;
import world.entities.Projectile;
import gui.graphics.CustomColors;
import orpheus.core.world.occupants.players.Player;

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
    public void hit(Projectile hittingProjectile, Player p){
        super.hit(hittingProjectile, p);
        p.knockBack(getRange().getInPixels(), hittingProjectile.getFacing(), Settings.seconds(1));
    }
    
    @Override
    public BoulderToss copy(){
        return new BoulderToss();
    }
}
