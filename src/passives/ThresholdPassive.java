package passives;

import upgradables.Stat;
import resources.Number;

public class ThresholdPassive extends AbstractPassive{
	/*
	 * Triggers so long as the user is below a set percentage
	 * of their maximum HP
	 */
	
	public ThresholdPassive(String n, int baseThresh){
		super(PassiveType.THRESHOLD, n, true);
		setStat(PassiveStat.THRESHOLD, baseThresh);
	}
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), getBase(PassiveStat.THRESHOLD));
		copyInflictTo(copy);
		return copy;
	}
	public void setStat(PassiveStat n, int value){
		switch(n){
		case THRESHOLD:
			int base = Number.minMax(1, value, 5);
			double thresh = (1.0 / (8 - base)) * 100;
			/*
			 * 1: 1/7 (14%)
			 * 2: 1/6 (17%)
			 * 3: 1/5 (20%)
			 * 4: 1/4 (25%)
			 * 5: 1/3 (33%)
			 */
			addStat(new Stat<PassiveStat>(PassiveStat.THRESHOLD, thresh));
			setBase(PassiveStat.THRESHOLD, base);
			break;
		}
	}
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= getStatValue(PassiveStat.THRESHOLD)){
			applyEffect(getRegisteredTo());
		}
	}
	public String getDescription(){
		String desc = getName() + ": ";
		desc += "When the user is at or below " + getStatValue(PassiveStat.THRESHOLD) + "% ";
		desc += "of their maximum HP, inflicts them with: ";
		desc += getInflict().getStatusString();
		return desc;
	}
}
