package passives;

import statuses.Status;

public class ThresholdPassive extends AbstractPassive{
	private double threshold;
	
	public ThresholdPassive(String n, double percent){
		super(PassiveType.THRESHOLD, n, true);
		threshold = percent;
	}
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), threshold);
		for(Status s : getInflicts()){
			copy.addStatus(s);
		}
		return copy;
	}
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= threshold){
			applyEffect(getRegisteredTo());
		}
	}
}
