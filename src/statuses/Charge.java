package statuses;

import actions.OnUpdateAction;
import entities.Player;
import initializers.Master;

public class Charge extends Status{
	public Charge(int lv, int dur){
		super(StatusName.CHARGE, "Charge", lv, Master.seconds(dur));
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel())); // make scale with frame count
				use();
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
	public String getDesc(){
		return "Charge, granting the inflicted \n" + 2.5 * getIntensityLevel() + " extra energy every frame for " + Master.framesToSeconds(getUses()) + " seconds"; 
	}
}
