package passives;

import statuses.Regeneration;

public class Leechhealer extends OnMeleeHitPassive{
	public Leechhealer(){
		super("Leechhealer", 20);
	}
	public void applyEffect(){
		getPlayer().inflict(new Regeneration(1, 20));
	}
}
