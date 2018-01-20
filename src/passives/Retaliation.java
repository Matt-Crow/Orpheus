package passives;

import statuses.Strength;

public class Retaliation extends ThreshholdPassive{
	public Retaliation(){
		super("Retaliation", 25);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Strength(2, 1));
	}
}
