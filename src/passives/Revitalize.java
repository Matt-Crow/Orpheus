package passives;

import statuses.Regeneration;

public class Revitalize extends ThreshholdPassive{
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
