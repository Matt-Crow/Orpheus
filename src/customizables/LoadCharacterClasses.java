package customizables;

import graphics.CustomColors;

public class LoadCharacterClasses {
	public static void load(){
		CharacterClass fire = new CharacterClass("Fire", CustomColors.fireColors, 2, 4, 5, 3, 3);
		CharacterClass air = new CharacterClass("Air", CustomColors.airColors, 2, 4, 3, 1, 5);
		CharacterClass earth = new CharacterClass("Earth", CustomColors.earthColors, 4, 1, 4, 4, 1);
		CharacterClass water = new CharacterClass("Water", CustomColors.waterColors, 5, 4, 1, 3, 3);
		
		CharacterClass.addCharacterClasses(
				new CharacterClass[]{
						fire,
						air,
						earth,
						water
				}
				);
	}
}
