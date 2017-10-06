package statuses;

import entities.Player;
import resources.OnUpdateAction;

public class Regeneration extends Status{
	public Regeneration(int lv, int dur){
		super("Regeneration", lv, dur);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().healPerc(0.125 * getIntensityLevel());
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
}
