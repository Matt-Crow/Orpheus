package statuses;

import actions.OnUpdateAction;
import entities.Player;
import initializers.Master;

public class Regeneration extends Status{
	public Regeneration(int lv, int dur){
		super(StatusName.REGENERATION, "Regeneration", lv, Master.seconds(dur));
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().healPerc(5.0 * getIntensityLevel() / Master.FPS);
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
	public String getDesc(){
		return "Regeneration, restoring " + (5.0 * getIntensityLevel()) + "% of the inflicted's HP \n every second for " + Master.framesToSeconds(getUses()) + " seconds";
	}
}
