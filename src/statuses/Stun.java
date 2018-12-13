package statuses;

import actions.OnUpdateAction;
import resources.Number;
import entities.Player;
import initializers.Master;

public class Stun extends AbstractStatus{
	public Stun(int lv, int dur){
		super(StatusName.STUN, "Stun", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3)));
		// 1-3 seconds of -0.25 to -0.75 movement speed
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
	public String getDesc(){
		return "Stun, lowering the inflicted's movement speed by " + (25 * getIntensityLevel()) + "% for " + Master.framesToSeconds(getBaseUses()) + " seconds";
	}
}
