package passives;

public class ThreshholdPassive extends Passive{
	private double threshhold;
	public ThreshholdPassive(String n, double percent){
		super(n, "threshhold");
		threshhold = percent;
	}
	public void update(){
		if(getPlayer().getHPPerc() <= threshhold){
			applyEffect();
		}
	}
}
