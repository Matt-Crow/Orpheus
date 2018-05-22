package customizables;

import java.awt.Color;

import actives.*;

public class Air extends CharacterClass{
	public Air(){
		super("Air", Color.yellow, 2, 4, 3, 1, 5);
		addPossibleActive("Mini Windbolt");
		//addPossibleActive(new Tornado());
		addPossibleActive("Speed Test");
	}
}
