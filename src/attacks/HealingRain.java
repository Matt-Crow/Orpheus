package attacks;

import statuses.Regeneration;

public class HealingRain extends BoostAttack{
	public HealingRain(){
		super("Healing Rain", 25, 120, new Regeneration(1, 90));
	}
}
