package customizables;
import upgradables.AbstractUpgradable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import graphics.CustomColors;
import util.Number;
import util.Op;

// make this connect better with player somehow
public class CharacterClass extends AbstractUpgradable<CharacterStatName>{
    private CustomColors[] colors;

    private static HashMap<String, CharacterClass> allCharacterClasses = new HashMap<>();
    // initializers
    public CharacterClass(String n, CustomColors[] cs, int HP, int energy, int dmg, int reduction, int speed){
            super(n);
            colors = cs;

            setStat(CharacterStatName.HP, HP);
            setStat(CharacterStatName.ENERGY, energy);
            setStat(CharacterStatName.DMG, dmg);
            setStat(CharacterStatName.REDUCTION, reduction);
            setStat(CharacterStatName.SPEED, speed);
    }
    public CharacterClass copy(){
            return new CharacterClass(
                            getName(), 
                            getColors(), 
                            getBase(CharacterStatName.HP),
                            getBase(CharacterStatName.ENERGY),
                            getBase(CharacterStatName.DMG),
                            getBase(CharacterStatName.REDUCTION),
                            getBase(CharacterStatName.SPEED)
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
    public void setStat(CharacterStatName c, int value){
        value = Number.minMax(1, value, 5);
        switch(c){
        case HP:
            addStat(CharacterStatName.HP, value, 700 + 100 * value);
            break;
        case ENERGY:
            addStat(CharacterStatName.ENERGY, value, 12.5 * (value + 1));
            break;
        case DMG:
            addStat(CharacterStatName.DMG, value, 0.7 + 0.1 * value);
            break;
        case REDUCTION:
            // 1: 120%, 5: 80%
            addStat(CharacterStatName.REDUCTION, value, 1.3 - 0.1 * value);
            break;
        case SPEED:
            addStat(CharacterStatName.SPEED, value, (0.7 + 0.1 * value));
            break;
        }
    }
    public String getDescription(){
        return getName() + ": \n" 
                        + "Maximum hit points: " + getStatValue(CharacterStatName.HP) + "\n"
                        + "Maximum energy: " + getStatValue(CharacterStatName.ENERGY) + "\n"
                        + "Damage dealt modifier: " + getStatValue(CharacterStatName.DMG) + "\n"
                        + "Damage taken modifier: " + getStatValue(CharacterStatName.REDUCTION) + "\n"
                        + "Movement speed modifier: " + getStatValue(CharacterStatName.SPEED) + "\n";
    }
}
