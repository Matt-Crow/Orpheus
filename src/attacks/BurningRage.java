package attacks;

import statuses.Strength;
import statuses.Burn;

public class BurningRage extends BoostAttack{
	public BurningRage(){
		super("Burning Rage", 20, 100, new Strength(3, 10));
		addStatus(new Burn(2, 10));
	}
}