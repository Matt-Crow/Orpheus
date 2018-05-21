package actives;

import entities.ParticleType;
import graphics.CustomColors;

public class CursedDaggers extends ElementalAttack{
	public CursedDaggers(){
		super("Cursed Daggers", 2, 3, 5, 5, 0, 0);
		setParticleType(ParticleType.BEAM);
		setParticleColor(CustomColors.green);
	}
	public void use(){
		spawnArc(90, 7);
	}
}
