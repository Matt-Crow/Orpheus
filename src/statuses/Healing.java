package statuses;

import entities.Player;
import resources.OnUpdateAction;

public class Healing extends Status{
	public Healing(int lv){
		super("Healing", lv, 1);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().healPerc(100 / (6 - getIntensityLevel()));
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);;
	}
}
