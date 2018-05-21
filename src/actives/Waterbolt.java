package actives;

import entities.ParticleType;
import graphics.CustomColors;

public class Waterbolt extends ElementalAttack{
	public Waterbolt(){
		super("Waterbolt", 3, 3, 3, 3, 1, 2);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.waterColors);
	}
}
