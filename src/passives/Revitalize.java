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
			getPlayer().inflict(new Regeneration(2, 100));
			activated = true;
		}
	}
}
