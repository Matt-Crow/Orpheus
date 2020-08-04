package customizables.actives;

import util.Settings;
import entities.AbstractPlayer;
import entities.ParticleType;
import entities.Projectile;
import gui.graphics.CustomColors;

/**
 *
 * @author Matt Crow
 */
public class BoulderToss extends ElementalActive{
    public BoulderToss(){
        super("Boulder Toss", 1, 2, 2, 3, 4);
		setParticleType(ParticleType.BURST);
        setColors(CustomColors.earthColors);
    }
    
    @Override
    public void hit(Projectile hittingProjectile, AbstractPlayer p){
        super.hit(hittingProjectile, p);
        p.knockBack(getRange(), hittingProjectile.getDir(), Settings.seconds(1));
    }
    
    @Override
    public BoulderToss copy(){
        return new BoulderToss();
    }
}
