package statuses;

import actions.OnHitTrip;
import actions.OnHitKey;
import entities.Player;

public class Burn extends Status{
	public Burn(int lv, int uses){
		super(StatusName.BURN, "Burn", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				p.getLog().applyFilter(1 + 0.25 * getIntensityLevel());
				use();
			}
		};
		p.getActionRegister().addOnBeHit(a);
	}
	public String getDesc(){
		return "Burn, increasing the speed of damage taken by " + getIntensityLevel() * 25 + "% for the next " + getUses() + " hits recieved";
	}
}
