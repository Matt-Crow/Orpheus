package actives;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.Burn;

public class BlazingPillars extends ElementalAttack{
	public BlazingPillars(){
		super("Blazing Pillars", 3, 5, 5, 1, 0, 1);
		enableTracking();
		addStatus(new Burn(1, 5), 70);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.fireColors);
	}
	public void use(){
		super.use();
		spawnArc(360, 4);
	}
}
