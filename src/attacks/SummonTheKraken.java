package attacks;

import entities.ParticleType;
import resources.CustomColors;

public class SummonTheKraken extends ElementalAttack{
	public SummonTheKraken(){
		super("Summon the kraken", 1, 1, 0, 3, 5, 1);
		enableTracking();
		setParticleType(ParticleType.SHEAR);
		setColorBlend(CustomColors.waterColors);
	}
}
