package statuses;

import actions.*;
import entities.Player;
import util.Number;
import controllers.Master;
import java.util.function.UnaryOperator;

/**
 * Charge restores energy over time to the afflicted Player
 */
public class Charge extends AbstractStatus{
    private static final UnaryOperator<Integer> CALC = (i)->{return Master.seconds(Number.minMax(1, i, 3) + 2);};
    /**
     * When inflicted, restores energy to a Player over time.
     * @param lv 1-3. 2.5 energy restored per level per second.
     * @param dur Effect lasts (dur + 2) seconds.
     */
	public Charge(int lv, int dur){
		super(StatusName.CHARGE, lv, dur, CALC);
		// 2.5 to 7.5 energy per second for 3 to 5 seconds
	}
    
    @Override
	public void inflictOn(Player p){
		OnUpdateListener a = new OnUpdateListener(){
            @Override
			public void actionPerformed(OnUpdateEvent e){
				if(e.getUpdated() instanceof Player){
                    ((Player)e.getUpdated()).getEnergyLog().gainEnergy((int) (2.5 * getIntensityLevel() / Master.FPS)); // make scale with frame count
                }
			}
		};
		p.getActionRegister().addOnUpdate(a);
	}
    
    @Override
	public String getDesc(){
		return "Charge, granting the inflicted " + 2.5 * getIntensityLevel() + " extra energy every second for " + Master.framesToSeconds(getMaxUses()) + " seconds"; 
	}

    @Override
    public AbstractStatus copy() {
        return new Charge(getIntensityLevel(), getBaseParam());
    }
}
