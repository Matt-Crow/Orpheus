package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;

public class Resistance extends Status{
	public Resistance(int lv, int uses){
		super(StatusName.RESISTANCE, "Resistance", lv, uses);
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
		return "Resistance, reducing the speed of damage taken by " + (25 * getIntensityLevel()) + "% \n for the next " + getUses() + " hits received";
	}
}
