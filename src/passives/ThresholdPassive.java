package passives;

public class ThresholdPassive extends AbstractPassive{
	/*
	 * Triggers so long as the user is below a set percentage
	 * of their maximum HP
	 */
	private double threshold;
	
	public ThresholdPassive(String n, double percent){
		super(PassiveType.THRESHOLD, n, true);
		threshold = percent;
	}
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), threshold);
		copyInflictTo(copy);
		return copy;
	}
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= threshold){
			applyEffect(getRegisteredTo());
		}
	}
	public String getDescription(){
		String desc = getName() + ": \n";
		desc += "When the user is at or below " + threshold + "% \n";
		desc += "of their maximum HP, inflicts them with: \n";
		desc += getInflict().getStatusString();
		return desc;
	}
}
