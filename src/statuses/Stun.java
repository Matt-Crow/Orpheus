package statuses;

import entities.Player;
import resources.OnUpdateAction;

public class Stun extends Status{
	public Stun(int lv, int dur){
		super("Stun", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.applySpeedFilter(1.0 - 0.25 * getIntensityLevel());
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
}
