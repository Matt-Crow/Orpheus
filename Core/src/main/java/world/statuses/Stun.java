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
 * The Stun status decreases an AbstractEntity's movement speed
 */
public class Stun extends AbstractStatus implements EventListener<OnUpdateEvent> {
    private static final UnaryOperator<Integer> CALC = (i)->{return Settings.seconds(Number.minMax(1, i, 3));};
    /**
     * The Stun status will decrease an AbstractEntity's movement speed
     * @param lv 1-3, decreasing the AbstractEntity's movement speed by 25% per level.
     * @param dur how many seconds the status will last, also 1-3
     */
	public Stun(int lv, int dur){
		super("stun", lv, dur, CALC);
		// 1-3 seconds of -0.25 to -0.75 movement speed
	}
    
    @Override
	public void inflictOn(Player p){
		p.eventOnUpdate().add(this);
	}

    @Override
    public void removeFrom(Player p) {
        p.eventOnUpdate().remove(this);
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
    public void handle(OnUpdateEvent e) {
        Optional<Player> updated = CastUtil.cast(e.getUpdated());
        updated.orElseThrow(() -> new UnsupportedOperationException("Rush can only handle player updates"));
        updated.get().addSpeedBoost(-0.25 * getIntensityLevel());
        use();
    }
}
