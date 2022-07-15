package world.statuses;

import world.events.OnUpdateEvent;
import world.events.OnUpdateListener;
import world.entities.AbstractPlayer;
import util.Number;
import util.Settings;
import java.util.function.UnaryOperator;

/**
 * Regeneration restores an AbstractEntity's hit points over time.
 */
public class Regeneration extends AbstractStatus implements OnUpdateListener{
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3)) + 2;};
    /**
     * Creates the Regeneration status.
     * @param lv 1-3. The target restores 1.5% of their maximum HP per second for each level of the status. 
     * Ex: lv 3 will restore 4.5% per second.
     * @param dur effect lasts for (dur + 2) seconds.
     */
	public Regeneration(int lv, int dur){
		super(StatusName.REGENERATION, lv, dur, CALC);
		// 3 - 5 seconds of 1.5% - 4.5% healing each second
		// balance later
	}
    
    @Override
	public void inflictOn(AbstractPlayer p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Regeneration, restoring " + (1.5 * getIntensityLevel()) + "% of the inflicted's HP every second for " + Settings.framesToSeconds(getMaxUses()) + " seconds";
	}

    @Override
    public AbstractStatus copy() {
        return new Regeneration(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void trigger(OnUpdateEvent e) {
        if(e.getUpdated() instanceof AbstractPlayer){
            ((AbstractPlayer)e.getUpdated()).getLog().healPerc(2.5 * getIntensityLevel() / Settings.FPS);
        }
        use();
    }
}
