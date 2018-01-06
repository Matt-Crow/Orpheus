package attacks;

import entities.ParticleType;
import entities.Player;
import resources.CustomColors;
import statuses.Burn;

public class BlazingPillars extends ElementalAttack{
	public BlazingPillars(){
		super("Blazing Pillars", 3, 5, 5, 1, 0, 1);
		enableTracking();
		addStatus(new Burn(1, 60), 70);
		setParticleType(ParticleType.BURST);
		setColorBlend(CustomColors.fireColors);
	}
	public void use(Player user){
		super.use(user);
		spawnArc(user, 360, 4);
	}
}
