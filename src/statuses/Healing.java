package statuses;

import actions.OnUpdateAction;
import entities.Player;

public class Healing extends Status{
	public Healing(int lv){
		super(StatusName.HEALING, "Healing", lv, 1);
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().healPerc(100.0 / (6 - getIntensityLevel()));
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);;
	}
}
