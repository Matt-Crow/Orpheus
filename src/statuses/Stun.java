package statuses;

import actions.*;
import util.Number;
import entities.Player;
import controllers.Master;

/**
 * The Stun status decreases an Entity's movement speed
 */
public class Stun extends AbstractStatus{
    
    /**
     * The Stun status will decrease an Entity's movement speed
     * @param lv 1-3, decreasing the Entity's movement speed by 25% per level.
     * @param dur how many seconds the status will last, also 1-3
     */
	public Stun(int lv, int dur){
		super(StatusName.STUN, "Stun", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3)));
		// 1-3 seconds of -0.25 to -0.75 movement speed
	}
    
    @Override
	public void inflictOn(Player p){
		OnUpdateListener a = new OnUpdateListener(){
            @Override
			public void actionPerformed(OnUpdateEvent e){
				e.getUpdated().applySpeedFilter(1.0 - 0.25 * getIntensityLevel());
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Stun, lowering the inflicted's movement speed by " + (25 * getIntensityLevel()) + "% for " + Master.framesToSeconds(getBaseUses()) + " seconds";
	}
}
