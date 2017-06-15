package customizables;

import java.awt.Color;

public class Fire extends CharacterClass{
	public Fire(){
		super("Fire", Color.red);
		setHPData(2, 1, 5);
		setEnergyData(4, 3, 3, 5, 3);
	}
}
