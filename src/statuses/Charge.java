package statuses;

import actions.OnUpdateAction;
import entities.Player;
import resources.Number;
import initializers.Master;

public class Charge extends Status{
	public Charge(int lv, int dur){
		super(StatusName.CHARGE, "Charge", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3) + 2));
		// 2.5 to 7.5 energy per second for 3 to 5 seconds
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel() / Master.FPS)); // make scale with frame count
				use();
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
	public String getDesc(){
		return "Charge, granting the inflicted " + 2.5 * getIntensityLevel() + " extra energy every second for " + Master.framesToSeconds(getUses()) + " seconds"; 
	}
}
