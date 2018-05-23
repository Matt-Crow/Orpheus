package customizables;

import java.awt.Color;

public class Water extends CharacterClass{
	public Water(){
		super("Water", Color.blue, 5, 4, 1, 3, 3);
		addPossibleActive("Waterbolt");
		addPossibleActive("Healing Rain");
	}
}
