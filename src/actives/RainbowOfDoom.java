package actives;

import entities.ParticleType;
import graphics.CustomColors;

public class RainbowOfDoom extends ElementalActive{
	public RainbowOfDoom(){
		super("RAINBOW OF DOOM", 1, 5, 3, 5, 5, 1);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.rainbow);
	}
}
