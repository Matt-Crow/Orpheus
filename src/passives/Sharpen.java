package passives;

import statuses.Strength;

public class Sharpen extends OnHitPassive{
	public Sharpen(){
		super("Sharpen", 20);
	}
	public void applyEffect(){
		getPlayer().inflict(new Strength(1, 1));
	}
}
