package actives;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.Burn;

public class FieldsOfFire extends ElementalActive{
	public FieldsOfFire(){
		super("Fields of Fire", 3, 3, 0, 5, 3, 0);
		addStatus(new Burn(1, 3), 100);
		setParticleType(ParticleType.SHEAR);
		setColorBlend(CustomColors.fireColors);
	}
}