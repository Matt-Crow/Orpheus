package attacks;

import entities.ParticleType;
import resources.CustomColors;
import statuses.Daze;

public class MiniWindbolt extends ElementalAttack{
	public MiniWindbolt(){
		super("Mini Windbolt", 5, 10, 400, 20, 0, 1, 1, 100);
		addStatus(new Daze(1, 3), 15);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.airColors);
	}
}
