package customizables;
import attacks.*;
import java.awt.Color;

public class Earth extends CharacterClass{
	public Earth(){
		super("Earth", Color.green);
		setHPData(4, 2, 3);
		setEnergyData(1, 2, 1, 2, 4);
		addPossibleActive(new Rupture());
	}
}
