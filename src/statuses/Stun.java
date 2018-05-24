package statuses;

import actions.OnUpdateAction;
import entities.Player;
import initializers.Master;

public class Stun extends Status{
	public Stun(int lv, int dur){
		super(StatusName.STUN, "Stun", lv, Master.seconds(dur));
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
		return "Stun, lowering the inflicted's movement speed by " + (25 * getIntensityLevel()) + "% \n for " + Master.framesToSeconds(getUses()) + " seconds";
	}
}
