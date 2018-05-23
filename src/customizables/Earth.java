package customizables;
import java.awt.Color;

public class Earth extends CharacterClass{
	public Earth(){
		super("Earth", Color.green, 4, 1, 4, 4, 1);
		addPossiblePassive("Nature's Healing");
		addPossibleActive("Boulder Toss");
		addPossibleActive("Earthquake");
	}
}
