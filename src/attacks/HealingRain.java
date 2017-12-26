package attacks;

import statuses.Regeneration;

public class HealingRain extends BoostAttack{
	public HealingRain(){
		super("Healing Rain", 5, 5, new Regeneration(1, 90));
	}
}
