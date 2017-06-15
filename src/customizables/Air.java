package customizables;

import java.awt.Color;

public class Air extends CharacterClass{
	public Air(){
		super("Air", Color.yellow);
		setHPData(1, 2, 4);
		setEnergyData(4, 2, 5, 5, 2);
	}
}
