package customizables;
import passives.*;
import java.awt.Color;

import actives.*;

public class Earth extends CharacterClass{
	public Earth(){
		super("Earth", Color.green, 4, 1, 4, 4, 1);
		//addPossibleActive(new Rupture());
		addPossiblePassive(new NaturesHealing());
		addPossibleActive("Boulder Toss");
		addPossibleActive("Earthquake");
	}
}
