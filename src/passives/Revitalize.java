package passives;

import statuses.Regeneration;


// find some way to do this
public class Revitalize extends ThresholdPassive{
	private boolean activated;
	public Revitalize(){
		super("Revitalize", 25);
		activated = false;
	}
	public void applyEffect(){
		if(!activated){
			getRegisteredTo().inflict(new Regeneration(3, 5));
			activated = true;
		}
	}
}
