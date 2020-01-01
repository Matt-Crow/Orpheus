package statuses;

import actions.*;
import entities.AbstractPlayer;
import util.Number;
import controllers.Master;
import entities.HumanPlayer;
import java.util.function.UnaryOperator;

/**
 * Charge restores energy over time to the afflicted AbstractPlayer
 */
public class Charge extends AbstractStatus implements OnUpdateListener{
    private static final UnaryOperator<Integer> CALC = (i)->{return Master.seconds(Number.minMax(1, i, 3) + 2);};
    /**
     * When inflicted, restores energy to a AbstractPlayer over time.
     * @param lv 1-3. 2.5 energy restored per level per second.
     * @param dur Effect lasts (dur + 2) seconds.
     */
	public Charge(int lv, int dur){
		super(StatusName.CHARGE, lv, dur, CALC);
		// 2.5 to 7.5 energy per second for 3 to 5 seconds
	}
    
    @Override
	public void inflictOn(AbstractPlayer p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Charge, granting the inflicted " + 2.5 * getIntensityLevel() + " extra energy every second for " + Master.framesToSeconds(getMaxUses()) + " seconds"; 
	}

    @Override
    public AbstractStatus copy() {
        return new Charge(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void trigger(OnUpdateEvent e) {
        if(e.getUpdated() instanceof HumanPlayer){
            ((HumanPlayer)e.getUpdated()).getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel() / Master.FPS)); // make scale with frame count
        }
        use();
    }
}
