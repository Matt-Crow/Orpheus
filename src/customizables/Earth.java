package customizables;
import graphics.CustomColors;

public class Earth extends CharacterClass{
	public Earth(){
		super("Earth", CustomColors.earthColors, 4, 1, 4, 4, 1);
		addPossiblePassive("Nature's Healing");
	}
}
