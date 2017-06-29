package customizables;

import java.awt.Color;
import attacks.*;

public class Fire extends CharacterClass{
	public Fire(){
		super("Fire", Color.red);
		setHPData(2, 1, 5);
		setEnergyData(4, 3, 3, 5, 3);
		addPossibleActive(new Fireball());
		addPossibleActive(new FieldsOfFire());
	}
}
