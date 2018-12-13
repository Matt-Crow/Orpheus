package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;
import initializers.Master;
import resources.Direction;
import resources.Number;
import resources.Op;

public class Strength extends AbstractStatus{
	public Strength(int lv, int uses){
		super(StatusName.STRENGTH, "Strength", Number.minMax(1, lv, 3), Number.minMax(1, uses, 3) * 2 + 1);
		// 3 - 7 uses of 3.5% to 10.5% extra damage logged and knocks back lv units
	}
	public void inflictOn(Player p){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				Player target = (Player)t.getHit();
				target.getLog().logPercentageDamage(3.5 * getIntensityLevel());
                
                Direction angleBetween = Direction.getDegreeByLengths(p.getX(), p.getY(), target.getX(), target.getY());
                int magnitude = Master.UNITSIZE * getIntensityLevel();
                target.knockBack(magnitude, angleBetween, Master.seconds(1));
				use();
			}
		};
		p.getActionRegister().addOnMeleeHit(a);
	}
	public String getDesc(){
		return "Strength, causing the inflicted's next " + getBaseUses() + " melee attacks to deal an extra " + (3.5 * getIntensityLevel()) + "% of the target's maximum HP and knock them back " + (3.5 * getIntensityLevel()) + " units";
	}
}
