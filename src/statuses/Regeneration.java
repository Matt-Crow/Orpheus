package statuses;

import actions.*;
import entities.Player;
import util.Number;
import controllers.Master;
import java.util.function.UnaryOperator;

/**
 * Regeneration restores an Entity's hit points over time.
 */
public class Regeneration extends AbstractStatus{
    private static final UnaryOperator<Integer> CALC = (i)->{return Master.seconds(Number.minMax(1, i, 3)) + 2;};
    /**
     * Creates the Regeneration status.
     * @param lv 1-3. The target restores 2.5% of their maximum HP per second for each level of the status. 
     * Ex: lv 3 will restore 7.5% per second.
     * @param dur effect lasts for (dur + 2) seconds.
     */
	public Regeneration(int lv, int dur){
		super(StatusName.REGENERATION, lv, dur, CALC);
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
                }
			}
		};
		
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Regeneration, restoring " + (2.5 * getIntensityLevel()) + "% of the inflicted's HP every second for " + Master.framesToSeconds(getMaxUses()) + " seconds";
	}

    @Override
    public AbstractStatus copy() {
        return new Regeneration(getIntensityLevel(), getBaseParam());
    }
}
