package passives;

import statuses.Strength;
import statuses.Resistance;

public class Determination extends ThreshholdPassive{
	public Determination(){
		super("Determination", 25);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Strength(1, 1));
		getRegisteredTo().inflict(new Resistance(1, 1));
	}
}
