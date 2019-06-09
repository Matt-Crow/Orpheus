package customizables;
import serialization.JsonSerialable;
import upgradables.AbstractUpgradable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import graphics.CustomColors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import util.Number;
import static java.lang.System.out;
import java.util.NoSuchElementException;

// make this connect better with player somehow
public class CharacterClass extends AbstractUpgradable<CharacterStatName> implements JsonSerialable{
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
        if(!allCharacterClasses.containsKey(n.toUpperCase())){
            throw new NoSuchElementException("Character class with name " + n + " not found. Did you remember to call CharacterClass.addCharacterClass(...)?");
        }
        return allCharacterClasses.get(n.toUpperCase());
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
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = super.serializeJson();
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        JsonArrayBuilder cols = Json.createArrayBuilder();
        for(CustomColors c : colors){
            cols.add(c.toString());
        }
        b.add("type", "character class");
        b.add("colors", cols.build());
        return b.build();
    }
}
