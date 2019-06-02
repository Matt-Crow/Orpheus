package statuses;

import actions.*;
import entities.Player;
import util.Number;
import controllers.Master;

/**
 * Charge restores energy over time to the afflicted Player
 */
public class Charge extends AbstractStatus{
    
    /**
     * When inflicted, restores energy to a Player over time.
     * @param lv 1-3. 2.5 energy restored per level per second.
     * @param dur Effect lasts (dur + 2) seconds.
     */
	public Charge(int lv, int dur){
		super(StatusName.CHARGE, "Charge", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3) + 2));
		// 2.5 to 7.5 energy per second for 3 to 5 seconds
	}
    
    @Override
	public void inflictOn(Player p){
		OnUpdateListener a = new OnUpdateListener(){
            @Override
			public void actionPerformed(OnUpdateEvent e){
				if(e.getUpdated() instanceof Player){
                    ((Player)e.getUpdated()).getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel() / Master.FPS)); // make scale with frame count
                    use();
                }
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Charge, granting the inflicted " + 2.5 * getIntensityLevel() + " extra energy every second for " + Master.framesToSeconds(getBaseUses()) + " seconds"; 
	}
}
