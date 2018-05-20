package customizables;

import java.awt.Color;
import attacks.*;

public class Water extends CharacterClass{
	public Water(){
		super("Water", Color.blue, 5, 4, 1, 3, 3);
		addPossibleActive(new Waterbolt());
		addPossibleActive("Healing Rain");
		addPossibleActive(new SummonTheKraken());
	}
}
