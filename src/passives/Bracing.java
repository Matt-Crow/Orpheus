package passives;

import statuses.Resistance;

public class Bracing extends ThreshholdPassive{
	public Bracing(){
		super("Bracing", 25);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Resistance(2, 1));
	}
}
