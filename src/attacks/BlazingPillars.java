package attacks;

import entities.ParticleType;
import entities.Player;
import resources.CustomColors;
import resources.Direction;
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
		for(int i = 0; i < 3; i++){
			spawnProjectile(user);
			getRegisteredProjectile().setDir(new Direction(getRegisteredProjectile().getDir().getDegrees() - 90 * (i + 1)));;
		}
	}
}
