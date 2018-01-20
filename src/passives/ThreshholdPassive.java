package passives;

public class ThreshholdPassive extends Passive{
	private double threshhold;
	
	public ThreshholdPassive(String n, double percent){
		super(n);
		threshhold = percent;
	}
	public void update(){
		if(getRegisteredTo().getLog().getHPPerc() <= threshhold){
			applyEffect();
		}
	}
}
