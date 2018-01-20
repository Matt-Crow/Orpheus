package passives;

import statuses.Strength;

public class Sharpen extends OnMeleeHitPassive{
	public Sharpen(){
		super("Sharpen", 20);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Strength(1, 1));
	}
}
