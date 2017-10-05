package attacks;

import entities.ParticleType;
import resources.CustomColors;
import statuses.Burn;

public class MegaFirebolt extends ElementalAttack{
	public MegaFirebolt(){
		super("Mega Firebolt", 20, 40, 700, 15, 50, 1, 1, 300);
		addStatus(new Burn(1, 20), 33);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.fireColors);
	}
}
