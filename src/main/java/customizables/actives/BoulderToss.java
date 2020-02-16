package customizables.actives;

import controllers.Master;
import entities.AbstractPlayer;
import entities.ParticleType;
import entities.SeedProjectile;
import graphics.CustomColors;

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
    public SeedProjectile createProjectile(){
        SeedProjectile ret = super.createProjectile();
        ret.getActionRegister().addOnHit((e)->{
            if(e.getWasHit() instanceof AbstractPlayer){
                ((AbstractPlayer)e.getWasHit()).knockBack(ret.getAttack().getRange(), ret.getDir(), Master.seconds(1));
            }
        });
        return ret;
    }
    
    @Override
    public BoulderToss copy(){
        return new BoulderToss();
    }
}
