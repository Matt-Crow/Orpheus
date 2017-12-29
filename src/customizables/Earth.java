package customizables;
import attacks.*;
import passives.*;
import java.awt.Color;

public class Earth extends CharacterClass{
	public Earth(){
		super("Earth", Color.green, 4, 1, 4, 4, 1);
		addPossibleActive(new Rupture());
		addPossiblePassive(new NaturesHealing());
		addPossibleActive(new BoulderToss());
		addPossibleActive(new Earthquake());
	}
}
