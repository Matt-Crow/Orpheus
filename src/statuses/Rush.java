package statuses;

import entities.Player;
import initializers.Master;
import resources.OnUpdateAction;

public class Rush extends Status{
	public Rush(int lv, int dur){
		super("Rush", lv, Master.seconds(dur));
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
}
