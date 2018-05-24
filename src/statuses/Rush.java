package statuses;

import actions.OnUpdateAction;
import entities.Player;
import initializers.Master;

public class Rush extends Status{
	public Rush(int lv, int dur){
		super(StatusName.RUSH, "Rush", lv, Master.seconds(dur));
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
		return "Rush, increasing the inflicted's movement speed by \n" + (20 * getIntensityLevel()) + "% for the next \n" + Master.framesToSeconds(getUses()) + " seconds";
	}
}
