package customizables;

import java.awt.Color;

public class Fire extends CharacterClass{
	public Fire(){
		super("Fire", Color.red, 2, 4, 5, 3, 3);
		addPossibleActive("Fireball");
		addPossibleActive("Fields of Fire");
		addPossibleActive("Burning Rage");
		addPossibleActive("Mega Firebolt");
		addPossibleActive("Blazing Pillars");
	}
}
