package statuses;

import actions.*;
import entities.Player;
import resources.Number;
import initializers.Master;

/**
 * The Rush status increases an Entity's movement speed
 */
public class Rush extends AbstractStatus{
	
    /**
     * Creates the Rush status.
     * @param lv 1-3. The afflicted Entity will receive a 20% increase in speed per level.
     * @param dur 1-3. Will last for (dur + 2) seconds.
     */
    public Rush(int lv, int dur){
		super(StatusName.RUSH, "Rush", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3) + 2));
		// 3 - 5 seconds of + 20% to 60% movement
	}
    
    @Override
	public void inflictOn(Player p){
		OnUpdateListener a = new OnUpdateListener(){
            @Override
			public void actionPerformed(OnUpdateEvent e){
				e.getUpdated().applySpeedFilter(1 + 0.2 * getIntensityLevel());
				use();
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Rush, increasing the inflicted's movement speed by " + (20 * getIntensityLevel()) + "% for the next " + Master.framesToSeconds(getBaseUses()) + " seconds";
	}
}
