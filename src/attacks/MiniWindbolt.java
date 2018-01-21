package attacks;

import entities.ParticleType;
import graphics.CustomColors;

public class MiniWindbolt extends ElementalAttack{
	public MiniWindbolt(){
		super("Mini Windbolt", 1, 1, 5, 5, 0, 1);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.airColors);
	}
}
