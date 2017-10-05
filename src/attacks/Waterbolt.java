package attacks;

import entities.ParticleType;
import resources.CustomColors;

public class Waterbolt extends ElementalAttack{
	public Waterbolt(){
		super("Waterbolt", 15, 60, 500, 10, 0, 1, 1, 250);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.waterColors);
	}
}
