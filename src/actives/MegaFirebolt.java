package actives;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.Burn;

public class MegaFirebolt extends ElementalActive{
	public MegaFirebolt(){
		super("Mega Firebolt", 4, 4, 3, 3, 3, 4);
		addStatus(new Burn(1, 5), 33);
		setParticleType(ParticleType.SHEAR);
		setColorBlend(CustomColors.fireColors);
	}
}
