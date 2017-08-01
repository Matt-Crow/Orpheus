package attacks;

import statuses.Healing;

public class Heal extends BoostAttack{
	public Heal(){
		super("Heal", 20, 120, new Healing(2));
	}
}
