package world.builds.actives;

import util.Settings;
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
        super("Boulder Toss", Arc.NONE, Range.SHORT, 2, Range.MEDIUM, 4);
		setParticleType(ParticleType.BURST);
        setColors(CustomColors.EARTH_COLORS);
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
