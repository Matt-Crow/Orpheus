package customizables;
import upgradables.AbstractUpgradable;
import upgradables.Stat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import graphics.CustomColors;
import resources.Number;
import resources.Op;

// make this connect better with player somehow
public class CharacterClass extends AbstractUpgradable<CharacterStat>{
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
				getBase(CharacterStat.HP),
				getBase(CharacterStat.ENERGY),
				getBase(CharacterStat.DMG),
				getBase(CharacterStat.REDUCTION),
				getBase(CharacterStat.SPEED)
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
	public static CharacterClass[] getAll(){
		CharacterClass[] ret = new CharacterClass[allCharacterClasses.size()];
		Collection<CharacterClass> values = allCharacterClasses.values();
		int i = 0;
		for(CharacterClass cc : values){
			ret[i] = cc;
			i++;
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
	
	public void setColors(CustomColors[] cs){
		colors = cs;
	}
	public CustomColors[] getColors(){
		return colors;
	}
	public void setStat(CharacterStat c, int value){
		value = Number.minMax(1, value, 5);
		switch(c){
		case HP:
			addStat(new Stat<CharacterStat>(CharacterStat.HP, 700 + 100 * value, 2));
			setBase(CharacterStat.HP, value);
			break;
		case ENERGY:
			addStat(new Stat<CharacterStat>(CharacterStat.ENERGY, 12.5 * (value + 1), 2));
			setBase(CharacterStat.ENERGY, value);
			break;
		case DMG:
			addStat(new Stat<CharacterStat>(CharacterStat.DMG, 0.7 + 0.1 * value));
			setBase(CharacterStat.DMG, value);
			break;
		case REDUCTION:
			// 1: 120%, 5: 80%
			addStat(new Stat<CharacterStat>(CharacterStat.REDUCTION, 1.3 - 0.1 * value));
			setBase(CharacterStat.REDUCTION, value);
			break;
		case SPEED:
			addStat(new Stat<CharacterStat>(CharacterStat.SPEED, (0.7 + 0.1 * value)));
			setBase(CharacterStat.SPEED, value);
			break;
		}
	}
	public String getDescription(){
		return getName() + ": \n" 
				+ "Maximum hit points: " + getStatValue(CharacterStat.HP) + "\n"
				+ "Maximum energy: " + getStatValue(CharacterStat.ENERGY) + "\n"
				+ "Damage dealt modifier: " + getStatValue(CharacterStat.DMG) + "\n"
				+ "Damage taken modifier: " + getStatValue(CharacterStat.REDUCTION) + "\n"
				+ "Movement speed modifier: " + getStatValue(CharacterStat.SPEED) + "\n";
	}
}
