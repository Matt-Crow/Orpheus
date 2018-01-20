package passives;

import statuses.Regeneration;

public class Recover extends OnBeHitPassive{
	public Recover(){
		super("Recover", 20);
	}
	public void applyEffect(){
		getRegisteredTo().inflict(new Regeneration(1, 5));
	}
}
