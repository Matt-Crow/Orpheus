package attacks;

import entities.ParticleType;
import resources.CustomColors;
import statuses.Burn;

public class MegaFirebolt extends ElementalAttack{
	public MegaFirebolt(){
		super("Mega Firebolt", 4, 4, 3, 3, 3, 4);
		addStatus(new Burn(1, 5), 33);
		setParticleType(ParticleType.SHEAR);
		setColorBlend(CustomColors.fireColors);
	}
}
