package world.statuses;

import world.events.EventListener;
import world.events.OnUpdateEvent;
import util.Number;
import util.Settings;

import java.util.Optional;
import java.util.function.UnaryOperator;

import orpheus.core.utils.CastUtil;
import orpheus.core.world.occupants.players.Player;

/**
 * The Rush status increases an AbstractEntity's movement speed
 */
public class Rush extends AbstractStatus implements EventListener<OnUpdateEvent> {
	private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3) + 2);};
    /**
     * Creates the Rush status.
     * @param lv 1-3. The afflicted AbstractEntity will receive a 50% increase in speed per level.
     * @param dur 1-3. Will last for (dur + 2) seconds.
     */
    public Rush(int lv, int dur){
		super("rush", lv, dur, CALC);
		// 3 - 5 seconds of + 20% to 60% movement
	}
    
    @Override
	public void inflictOn(Player p){
		p.getActionRegister().addOnUpdate(this);
	}
    
    @Override
	public String getDesc(){
		return "Rush, increasing the inflicted's movement speed by " + (50 * getIntensityLevel()) + "% for the next " + Settings.framesToSeconds(getMaxUses()) + " seconds";
	}

    @Override
    public AbstractStatus copy() {
        return new Rush(getIntensityLevel(), getBaseParam());
    }

    @Override
    public void handle(OnUpdateEvent e) {
        Optional<Player> updated = CastUtil.cast(e.getUpdated());
        updated.orElseThrow(() -> new UnsupportedOperationException("Rush can only handle player updates"));
        updated.get().multiplySpeedBy(1 + 0.5 * getIntensityLevel());
        use();
    }
}
