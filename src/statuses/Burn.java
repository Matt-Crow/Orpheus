package statuses;

import entities.Player;
import resources.OnHitAction;

public class Burn extends Status{
	public Burn(int lv, int uses){
		super("Burn", lv, uses);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction(){
			public void f(){
				p.getLog().applyFilter(1 + 0.25 * getIntensityLevel());
				use();
			}
		};
		p.getActionRegister().addOnBeHit(a);
	}
}
