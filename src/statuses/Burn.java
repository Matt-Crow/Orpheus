package statuses;

import entities.Player;
import resources.OnUpdateAction;

public class Burn extends Status{
	public Burn(int lv, int uses){
		super("Burn", lv, uses);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().applyFilter(1 + 0.25 * getIntensityLevel());
				use();
			}
		};
		
		p.addOnUpdate(a);
	}
}
