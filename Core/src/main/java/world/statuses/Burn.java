package world.statuses;

import world.events.EventListener;
import world.events.OnUpdateEvent;
import util.Settings;

import java.util.function.UnaryOperator;

import orpheus.core.utils.UndoableOperation;
import orpheus.core.world.occupants.players.Player;

/**
 *
 * @author Matt
 */
public class Burn  extends AbstractStatus implements EventListener<OnUpdateEvent> {
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(util.Number.minMax(1, i, 3) * 2 + 1);};
    
    public Burn(int lv, int uses){
        super("burn", lv, uses, CALC);
    }
    
    @Override
    public UndoableOperation<Player> getInflictor() {
        return makeEventBinder(p -> p.eventOnUpdate(), this);
    }
    
    @Override
	public String getDesc(){
		return "Burn, increasing the speed of damage taken by " + (25 * getIntensityLevel()) + "% for the next " + getMaxUses() + " seconds";
	}
    
    @Override
    public AbstractStatus copy() {
        return new Burn(getIntensityLevel(), getBaseParam());
    }
    
    @Override
    public void handle(OnUpdateEvent t) {
        ((Player)t.getUpdated()).getDamage().applyFilter(1 + 0.25 * getIntensityLevel());
        use();
    }
}
