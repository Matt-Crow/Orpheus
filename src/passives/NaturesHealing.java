package passives;

import statuses.Regeneration;

public class NaturesHealing extends OnBeHitPassive{
	public NaturesHealing(){
		super("Nature's Healing", 100);
	}
	public void applyEffect(){
		getPlayer().inflict(new Regeneration(1, 1));
	}
}
