package statuses;

import actions.OnUpdateAction;
import entities.Player;
import initializers.Master;

public class Charge extends Status{
	public Charge(int lv, int dur){
		super("Charge", lv, Master.seconds(dur));
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel()));
				use();
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
}
