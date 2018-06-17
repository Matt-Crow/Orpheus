package statuses;

import actions.OnUpdateAction;
import entities.Player;
import resources.Number;
import initializers.Master;

public class Regeneration extends Status{
	public Regeneration(int lv, int dur){
		super(StatusName.REGENERATION, "Regeneration", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3)) + 2);
		// 3 - 5 seconds of 2.5% - 7.5% healing each second
		// balance later
	}
	public void inflictOn(Player p){
		OnUpdateAction a = new OnUpdateAction(){
			public void f(){
				p.getLog().healPerc(2.5 * getIntensityLevel() / Master.FPS);
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
	public String getDesc(){
		return "Regeneration, restoring " + (2.5 * getIntensityLevel()) + "% of the inflicted's HP every second for " + Master.framesToSeconds(getUses()) + " seconds";
	}
}
