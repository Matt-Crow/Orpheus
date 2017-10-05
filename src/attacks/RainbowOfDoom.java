package attacks;

import entities.ParticleType;
import resources.CustomColors;

public class RainbowOfDoom extends ElementalAttack{
	public RainbowOfDoom(){
		super("RAINBOW OF DOOM", 0, 120, 600, 3, 250, 1, 1, 1);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.rainbow);
	}
}
