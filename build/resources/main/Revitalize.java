//package customizables.passives;

import statuses.Regeneration;


// find some way to do this
public class Revitalize extends customizables.passives.ThresholdPassive{
	private boolean activated;
	public Revitalize(){
		super("Revitalize", 25);
		activated = false;
	}
	public void applyEffect(){
		if(!activated){
			getUser().inflict(new Regeneration(3, 5));
			activated = true;
		}
	}
}
