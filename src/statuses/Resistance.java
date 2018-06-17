package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;
import resources.Number;

public class Resistance extends Status{
	public Resistance(int lv, int uses){
		super(StatusName.RESISTANCE, "Resistance", Number.minMax(1, lv, 3), Number.minMax(1, uses, 3) * 2 + 1);
		// make this stronger
	}
	public void inflictOn(Player p){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				p.getLog().applyFilter(1 - 0.25 * getIntensityLevel());
				use();
			}
		};
		p.getActionRegister().addOnBeHit(a);
	}
	public String getDesc(){
		return "Resistance, reducing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getUses() + " hits received";
	}
}
