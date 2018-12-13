package statuses;

import actions.OnUpdateAction;
import entities.Player;
import resources.Number;
import initializers.Master;

public class Rush extends AbstractStatus{
	public Rush(int lv, int dur){
		super(StatusName.RUSH, "Rush", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3) + 2));
		// 3 - 5 seconds of + 20% to 60% movement
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.applySpeedFilter(1 + 0.2 * getIntensityLevel());
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
	public String getDesc(){
		return "Rush, increasing the inflicted's movement speed by " + (20 * getIntensityLevel()) + "% for the next " + Master.framesToSeconds(getBaseUses()) + " seconds";
	}
}
