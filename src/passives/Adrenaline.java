package passives;

import statuses.Charge;

public class Adrenaline extends ThreshholdPassive{
	public Adrenaline(){
		super("Adrenaline", 25);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Charge(2, 1));
	}
}
