package attacks;

import statuses.Strength;

public class BladeStance extends BoostAttack{
	public BladeStance(){
		super("Blade Stance", 10, 60, new Strength(2, 7));
	}
}
