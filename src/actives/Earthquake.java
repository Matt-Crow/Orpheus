package actives;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.Stun;

public class Earthquake extends ElementalActive{
	public Earthquake(){
		super("Earthquake", 4, 5, 0, 2, 3, 0);
		addStatus(new Stun(3, 3), 100);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.earthColors);
	}
}
