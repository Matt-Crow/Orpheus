package attacks;

import entities.ParticleType;
import resources.CustomColors;

public class RainbowOfDoom extends ElementalAttack{
	public RainbowOfDoom(){
		super("RAINBOW OF DOOM", 1, 5, 3, 5, 5, 1);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.rainbow);
	}
}
