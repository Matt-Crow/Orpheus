package customizables;
import upgradables.AbstractUpgradable;
import upgradables.Stat;

import java.util.HashMap;
import java.util.Set;

import graphics.CustomColors;
import resources.Number;
import resources.Op;

// make this connect better with player somehow
public class CharacterClass extends AbstractUpgradable{
	private CustomColors[] colors;
	
	private static HashMap<String, CharacterClass> allCharacterClasses = new HashMap<>();
	// initializers
	public CharacterClass(String n, CustomColors[] cs, int HP, int energy, int dmg, int reduction, int speed){
		super(n);
		colors = cs;
		
		setStat(CharacterStat.HP, HP);
		setStat(CharacterStat.ENERGY, energy);
		setStat(CharacterStat.DMG, dmg);
		setStat(CharacterStat.REDUCTION, reduction);
		setStat(CharacterStat.SPEED, speed);
	}
	public CharacterClass copy(){
		return new CharacterClass(
				getName(), 
				getColors(), 
				getBase("maxHP"),
				getBase("Max energy"),
				getBase("damage dealt modifier"),
				getBase("damage taken modifier"),
				getBase("speed")
			);
	}
	
	// static methods
	public static void addCharacterClass(CharacterClass c){
		allCharacterClasses.put(c.getName().toUpperCase(), c);
	}
	public static void addCharacterClasses(CharacterClass[] c){
		for(CharacterClass cs : c){
			addCharacterClass(cs);
		}
	}
	public static CharacterClass getCharacterClassByName(String n){
		CharacterClass ret = allCharacterClasses.getOrDefault(n.toUpperCase(), 
				new CharacterClass("ERROR", new CustomColors[0], 3, 3, 3, 3, 3));
		if(!n.toUpperCase().equals(ret.getName().toUpperCase())){
			Op.add("No character class was found with name " + n + " in CharacterClass.getCharacterClassByName");
			Op.dp();
		}
		return ret;
	}
	public static String[] getAllNames(){
		String[] ret = new String[allCharacterClasses.size()];
		Set<String> keys = allCharacterClasses.keySet();
		int i = 0;
		for(String key : keys){
			ret[i] = key;
			i++;
		}
		return ret;
	}
	// getters
	public CustomColors[] getColors(){
		return colors;
	}
	public void setStat(CharacterStat c, int value){
		value = Number.minMax(1, value, 5);
		switch(c){
		case HP:
			addStat(new Stat("maxHP", 700 + 100 * value, 2));
			setBase("maxHP", value);
			break;
		case ENERGY:
			addStat(new Stat("Max energy", 12.5 * (value + 1), 2));
			setBase("Max energy", value);
			break;
		case DMG:
			addStat(new Stat("damage dealt modifier", 0.7 + 0.1 * value));
			setBase("damage dealt modifier", value);
			break;
		case REDUCTION:
			// 1: 120%, 5: 80%
			addStat(new Stat("damage taken modifier", 1.3 - 0.1 * value));
			setBase("damage taken modifier", value);
			break;
		case SPEED:
			addStat(new Stat("speed", (0.7 + 0.1 * value)));
			setBase("speed", value);
			break;
		}
	}
}
