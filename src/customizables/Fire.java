package customizables;

import java.awt.Color;
import attacks.*;

public class Fire extends CharacterClass{
	public Fire(){
		super("Fire", Color.red, 2, 4, 5, 3, 3);
		addPossibleActive(new Fireball());
		addPossibleActive(new FieldsOfFire());
		addPossibleActive(new BurningRage());
		addPossibleActive(new MegaFirebolt());
		addPossibleActive(new BlazingPillars());
	}
}
