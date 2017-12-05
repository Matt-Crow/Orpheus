package customizables;

import java.awt.Color;
import attacks.*;

public class Air extends CharacterClass{
	public Air(){
		super("Air", Color.yellow);
		setHPData(1, 2, 4);
		setEnergyData(4, 2, 5, 5, 2);
		addPossibleActive(new MiniWindbolt());
		addPossibleActive(new Tornado());
	}
}
