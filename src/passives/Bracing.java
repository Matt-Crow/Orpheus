package passives;

import statuses.Resistance;

public class Bracing extends ThreshholdPassive{
	public Bracing(){
		super("Bracing", 50);
	}
	public void applyEffect(){
		getPlayer().inflict(new Resistance(2, 1));
	}
}
