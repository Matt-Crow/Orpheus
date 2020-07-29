package statuses;

import events.OnUpdateEvent;
import events.OnUpdateListener;
import util.Number;
import entities.AbstractPlayer;
import controllers.Settings;
import java.util.function.UnaryOperator;

/**
 * The Stun status decreases an AbstractEntity's movement speed
 */
public class Stun extends AbstractStatus implements OnUpdateListener{
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3));};
    /**
     * The Stun status will decrease an AbstractEntity's movement speed
     * @param lv 1-3, decreasing the AbstractEntity's movement speed by 25% per level.
     * @param dur how many seconds the status will last, also 1-3
     */
	public Stun(int lv, int dur){
		super(StatusName.STUN, lv, dur, CALC);
		// 1-3 seconds of -0.25 to -0.75 movement speed
	}
    
    @Override
	public void inflictOn(AbstractPlayer p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Stun, lowering the inflicted's movement speed by " + (25 * getIntensityLevel()) + "% for " + Settings.framesToSeconds(getMaxUses()) + " seconds";
	}

    @Override
    public AbstractStatus copy() {
        return new Stun(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void trigger(OnUpdateEvent e) {
        e.getUpdated().applySpeedFilter(1.0 - 0.25 * getIntensityLevel());
        use();
    }
}
