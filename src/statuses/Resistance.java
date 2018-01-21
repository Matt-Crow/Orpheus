package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;

public class Resistance extends Status{
	public Resistance(int lv, int uses){
		super("Resistance", lv, uses);
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
}
