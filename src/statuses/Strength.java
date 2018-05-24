package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;
import resources.Direction;

public class Strength extends Status{
	public Strength(int lv, int uses){
		super(StatusName.STRENGTH, "Strength", lv, uses);
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
		return "Strength, causing the inflicted's next " + getUses() + " melee attacks to \n" + "deal an extra " + (3.5 * getIntensityLevel()) + "% of the target's maximum HP \n and knock them back " + (3.5 * getIntensityLevel()) + " units";
	}
}
