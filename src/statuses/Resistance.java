package statuses;

import actions.*;
import entities.Player;
import util.Number;

/**
 * Resistance slows how fast an Entity takes damage, giving them a longer lifespan.
 * Note that this does not decrease the damage they take.
 * 
 * Needs reworking.
 * @see battle.DamageBacklog
 */
public class Resistance extends AbstractStatus{
    
    /**
     * 
     * @param lv 1-3. Slows damage by 25% per level.
     * Example: at level 1, a Player's lifespan is increased from 5 seconds minimum to 6.25 seconds.
     * @param uses lasts for ((uses * 2) + 1) hits received.
     */
	public Resistance(int lv, int uses){
		super(StatusName.RESISTANCE, "Resistance", Number.minMax(1, lv, 3), Number.minMax(1, uses, 3) * 2 + 1);
		// make this stronger
	}
    
    @Override
	public void inflictOn(Player p){
		OnHitListener a = new OnHitListener(){
            @Override
			public void actionPerformed(OnHitEvent t){
                if(t.getWasHit() instanceof Player){
                    ((Player)t.getWasHit()).getLog().applyFilter(1 - 0.25 * getIntensityLevel());
                    use();
                }
			}
		};
		p.getActionRegister().addOnBeHit(a);
	}
    
    @Override
	public String getDesc(){
		return "Resistance, reducing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getBaseUses() + " hits received";
	}

    @Override
    public AbstractStatus copy() {
        return new Resistance(getIntensityLevel(), getBaseUses());
    }
}
