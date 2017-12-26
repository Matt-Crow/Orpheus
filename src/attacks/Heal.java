package attacks;

import statuses.Healing;

public class Heal extends BoostAttack{
	public Heal(){
		super("Heal", 5, 5, new Healing(2));
	}
}
