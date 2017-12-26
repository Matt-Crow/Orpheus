package attacks;

import statuses.Resistance;

public class ShieldStance extends BoostAttack{
	public ShieldStance(){
		super("Shield Stance", 1, 3, new Resistance(2, 7));
	}
}
