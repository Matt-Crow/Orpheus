package statuses;

import actions.*;
import entities.Player;
import resources.Number;
import initializers.Master;

/**
 * Regeneration restores an Entity's hit points over time.
 */
public class Regeneration extends AbstractStatus{
    
    /**
     * Creates the Regeneration status.
     * @param lv 1-3. The target restores 2.5% of their maximum HP per second for each level of the status. 
     * Ex: lv 3 will restore 7.5% per second.
     * @param dur effect lasts for (dur + 2) seconds.
     */
	public Regeneration(int lv, int dur){
		super(StatusName.REGENERATION, "Regeneration", Number.minMax(1, lv, 3), Master.seconds(Number.minMax(1, dur, 3)) + 2);
		// 3 - 5 seconds of 2.5% - 7.5% healing each second
		// balance later
	}
    
    @Override
	public void inflictOn(Player p){
		OnUpdateListener a = new OnUpdateListener(){
            @Override
			public void actionPerformed(OnUpdateEvent e){
                if(e.getUpdated() instanceof Player){
                    ((Player)e.getUpdated()).getLog().healPerc(2.5 * getIntensityLevel() / Master.FPS);
                    use();
                }
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Regeneration, restoring " + (2.5 * getIntensityLevel()) + "% of the inflicted's HP every second for " + Master.framesToSeconds(getBaseUses()) + " seconds";
	}
}
