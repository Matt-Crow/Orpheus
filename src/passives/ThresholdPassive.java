package passives;

import statuses.StatusTable;

public class ThresholdPassive extends AbstractPassive{
	private double threshold;
	
	public ThresholdPassive(String n, double percent){
		super(PassiveType.THRESHOLD, n, true);
		threshold = percent;
	}
	public ThresholdPassive copy(){
		ThresholdPassive copy = new ThresholdPassive(getName(), threshold);
		StatusTable orig = getInflict();
		for(int i = 0; i < orig.getSize(); i++){
			copy.addStatus(orig.getNameAt(i), orig.getIntensityAt(i), orig.getDurationAt(i));
		}
		return copy;
	}
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= threshold){
			applyEffect(getRegisteredTo());
		}
	}
}
