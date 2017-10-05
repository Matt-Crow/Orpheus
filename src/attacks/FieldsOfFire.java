package attacks;

import entities.ParticleType;
import resources.CustomColors;
import statuses.Burn;

public class FieldsOfFire extends ElementalAttack{
	public FieldsOfFire(){
		super("Fields of Fire", 15, 50, 0, 10, 400, 1, 1, 175);
		addStatus(new Burn(1, 60), 100);
		setParticleType(ParticleType.SHEAR);
		setColorBlend(CustomColors.fireColors);
	}
}