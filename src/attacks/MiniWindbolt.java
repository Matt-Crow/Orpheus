package attacks;

import entities.ParticleType;
import resources.CustomColors;
import statuses.Daze;

public class MiniWindbolt extends ElementalAttack{
	public MiniWindbolt(){
		super("Mini Windbolt", 1, 1, 5, 5, 0, 1);
		addStatus(new Daze(1, 3), 15);
		setParticleType(ParticleType.BEAM);
		setColorBlend(CustomColors.airColors);
	}
}
