package attacks;

import statuses.Resistance;

public class ShieldStance extends BoostAttack{
	public ShieldStance(){
		super("Shield Stance", 10, 60, new Resistance(2, 7));
	}
}
