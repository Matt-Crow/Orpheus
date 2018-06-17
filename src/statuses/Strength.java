package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;
import resources.Direction;
import resources.Number;

public class Strength extends Status{
	public Strength(int lv, int uses){
		super(StatusName.STRENGTH, "Strength", Number.minMax(1, lv, 3), Number.minMax(1, uses, 3) * 2 + 1);
		// 3 - 7 uses of 3.5% to 10.5% extra damage logged and 
		//TODO: how does knockback work again?
	}
	public void inflictOn(Player p){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				Player target = (Player)t.getHit();
				target.getLog().logPercentageDamage(3.5 * getIntensityLevel());
				target.applyKnockback(new Direction(p.getDir().getDegrees()), 10, (int) (3.5 * getIntensityLevel()));
				use();
			}
		};
		p.getActionRegister().addOnMeleeHit(a);
	}
	public String getDesc(){
		return "Strength, causing the inflicted's next " + getUses() + " melee attacks to deal an extra " + (3.5 * getIntensityLevel()) + "% of the target's maximum HP and knock them back " + (3.5 * getIntensityLevel()) + " units";
	}
}
