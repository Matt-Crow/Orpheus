package customizables;

import java.awt.Color;
import attacks.*;

public class Water extends CharacterClass{
	public Water(){
		super("Water", Color.blue);
		setHPData(5, 3, 1);
		setEnergyData(3, 3, 2, 3, 4);
		
		addPossibleActive(new Waterbolt());
		addPossibleActive(new HealingRain());
	}
}
