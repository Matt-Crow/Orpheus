package passives;

import statuses.Regeneration;

public class Leechhealer extends OnMeleeHitPassive{
	public Leechhealer(){
		super("Leechhealer", 20);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Regeneration(1, 5));
	}
}
