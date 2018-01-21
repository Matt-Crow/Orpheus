package attacks;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.Stun;

public class BoulderToss extends ElementalAttack{
	public BoulderToss(){
		super("Boulder Toss", 5, 5, 2, 2, 3, 4);
		addStatus(new Stun(3, 4), 70);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.earthColors);
	}
}
