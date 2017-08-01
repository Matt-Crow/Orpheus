package passives;

import statuses.Regeneration;

public class Recover extends OnBeHitPassive{
	public Recover(){
		super("Recover", 20);
	}
	public void applyEffect(){
		getPlayer().inflict(new Regeneration(1, 20));
	}
}
