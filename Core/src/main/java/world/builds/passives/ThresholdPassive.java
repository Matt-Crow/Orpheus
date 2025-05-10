package world.builds.passives;

import world.events.EventListener;
import world.events.OnUpdateEvent;
import orpheus.core.world.occupants.players.Player;
import util.Number;

/**
 * Triggers so long as the user is below a set percentage
 * of their maximum HP
 */
public class ThresholdPassive extends AbstractPassive implements EventListener<OnUpdateEvent> {
	private final int threshold;
    private final int baseThreshold;
    
    public ThresholdPassive(String n, int baseThresh){
		super(n, true);
        baseThreshold = Number.minMax(1, baseThresh, 3);
        threshold = (int) ((1.0 / (6 - baseThreshold)) * 100);
	}
    @Override
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), baseThreshold);
		copyInflictTo(copy);
		return copy;
	}
    
    @Override
	public String getDescription(){
		var desc = "When the user is at or below " + (int)threshold + "% ";
		desc += "of their maximum HP, inflicts them with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public void init(){
        if(getUser() != null){
            getUser().eventOnUpdate().add(this);
        }
    }
    
    @Override
    public void handle(OnUpdateEvent e) {
        if(((Player)e.getUpdated()).getDamage().getHPPerc() <= threshold){
            trigger();
        }
    }

    public void trigger() {
        applyEffect(getUser());
    }
}
