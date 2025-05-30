package world.statuses;

import world.events.EventListener;
import world.events.OnUpdateEvent;
import util.Number;
import util.Settings;
import java.util.function.UnaryOperator;

import orpheus.core.utils.UndoableOperation;
import orpheus.core.world.occupants.players.Player;

/**
 * Regeneration restores an AbstractEntity's hit points over time.
 */
public class Regeneration extends AbstractStatus implements EventListener<OnUpdateEvent> {
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3)) + 2;};
    /**
     * Creates the Regeneration status.
     * @param lv 1-3. The target restores 1.5% of their maximum HP per second for each level of the status. 
     * Ex: lv 3 will restore 4.5% per second.
     * @param dur effect lasts for (dur + 2) seconds.
     */
	public Regeneration(int lv, int dur){
		super("regeneration", lv, dur, CALC);
		// 3 - 5 seconds of 1.5% - 4.5% healing each second
		// balance later
	}
    
    @Override
    public UndoableOperation<Player> getInflictor() {
        return makeEventBinder(p -> p.eventOnUpdate(), this);
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
    public void handle(OnUpdateEvent e) {
        if(e.getUpdated() instanceof Player){
            ((Player)e.getUpdated()).getDamage().healPerc(2.5 * getIntensityLevel() / Settings.FPS);
        }
        use();
    }
}
