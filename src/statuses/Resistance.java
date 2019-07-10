package statuses;

import actions.*;
import entities.Player;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import util.Number;

/**
 * Resistance slows how fast an Entity takes damage, giving them a longer lifespan.
 * Note that this does not decrease the damage they take.
 * 
 * Needs reworking.
 * @see battle.DamageBacklog
 */
public class Resistance extends AbstractStatus implements OnHitListener{
    private static final UnaryOperator<Integer> CALC = (i)->{return Number.minMax(1, i, 3) * 2 + 1;};
    /**
     * 
     * @param lv 1-3. Slows damage by 25% per level.
     * Example: at level 1, a Player's lifespan is increased from 5 seconds minimum to 6.25 seconds.
     * @param uses lasts for ((uses * 2) + 1) hits received.
     */
	public Resistance(int lv, int uses){
		super(StatusName.RESISTANCE, lv, uses, CALC);
		// make this stronger
	}
    
    @Override
	public void inflictOn(Player p){
		p.getActionRegister().addOnBeHit(this);
	}
    
    @Override
	public String getDesc(){
		return "Resistance, reducing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getMaxUses() + " hits received";
	}

    @Override
    public AbstractStatus copy() {
        return new Resistance(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void trigger(OnHitEvent t) {
        if(t.getWasHit() instanceof Player){
            ((Player)t.getWasHit()).getLog().applyFilter(1 - 0.25 * getIntensityLevel());
        }
    }
}
