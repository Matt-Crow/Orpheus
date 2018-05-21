package actives;

import entities.ParticleType;

public class Fireball extends ElementalAttack{
	public Fireball(){
		super("Fireball", 4, 5, 4, 3, 3, 4);
		setParticleType(ParticleType.BURST);
	}
}
