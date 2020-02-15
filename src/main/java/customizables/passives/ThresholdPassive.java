package customizables.passives;

import actions.OnUpdateEvent;
import actions.OnUpdateListener;
import entities.AbstractPlayer;
import util.Number;

/**
 * Triggers so long as the user is below a set percentage
 * of their maximum HP
 */
public class ThresholdPassive extends AbstractPassive implements OnUpdateListener{
	private final int threshold;
    private final int baseThreshold;
    
    public ThresholdPassive(String n, int baseThresh){
		super(PassiveType.THRESHOLD, n, true);
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
		String desc = getName() + ": ";
		desc += "When the user is at or below " + (int)threshold + "% ";
		desc += "of their maximum HP, inflicts them with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}

    @Override
    public void init(){
        super.init();
        if(getUser() != null){
            getUser().getActionRegister().addOnUpdate(this);
        }
    }
    
    @Override
    public void trigger(OnUpdateEvent e) {
        if(((AbstractPlayer)e.getUpdated()).getLog().getHPPerc() <= threshold){
            applyEffect((AbstractPlayer)e.getUpdated());
        }
    }
}
