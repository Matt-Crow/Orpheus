package world.statuses;

import world.events.EventListener;
import world.events.OnUpdateEvent;
import util.Settings;
import world.entities.AbstractPlayer;
import java.util.function.UnaryOperator;
import util.Number;

/**
 * Resistance slows how fast an AbstractEntity takes damage, giving them a longer lifespan.
 * Note that this does not decrease the damage they take.
 * 
 * Needs reworking.
 * @see world.battle.DamageBacklog
 */
public class Resistance extends AbstractStatus implements EventListener<OnUpdateEvent> {
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3) * 2 + 1);};
    /**
     * 
     * @param lv 1-3. Slows damage by 25% per level.
 Example: at level 1, a AbstractPlayer's lifespan is increased from 5 seconds minimum to 6.25 seconds.
     * @param uses lasts for ((uses * 2) + 1) hits received.
     */
	public Resistance(int lv, int uses){
		super(StatusName.RESISTANCE, lv, uses, CALC);
	}
    
    @Override
	public void inflictOn(AbstractPlayer p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Resistance, reducing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getMaxUses() + " seconds";
	}

    @Override
    public AbstractStatus copy() {
        return new Resistance(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void handle(OnUpdateEvent t) {
        ((AbstractPlayer)t.getUpdated()).getLog().applyFilter(1 - 0.25 * getIntensityLevel());
        use();
    }
}
