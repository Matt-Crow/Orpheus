package world.builds.actives;

import util.Settings;
import world.entities.AbstractPlayer;
import world.entities.ParticleType;
import world.entities.Projectile;
import gui.graphics.CustomColors;

/**
 *
 * @author Matt Crow
 */
public class BoulderToss extends ElementalActive{
    public BoulderToss(){
        super("Boulder Toss", 1, Range.SHORT, 2, Range.MEDIUM, 4);
		setParticleType(ParticleType.BURST);
        setColors(CustomColors.earthColors);
    }
    
    @Override
    public void hit(Projectile hittingProjectile, AbstractPlayer p){
        super.hit(hittingProjectile, p);
        p.knockBack(getRange().getInPixels(), hittingProjectile.getFacing(), Settings.seconds(1));
    }
    
    @Override
    public BoulderToss copy(){
        return new BoulderToss();
    }
}
