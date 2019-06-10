package customizables;

import java.util.*;
import javax.json.*;

import serialization.JsonSerialable;
import upgradables.AbstractUpgradable;
import graphics.CustomColors;
import util.Number;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractUpgradable<CharacterStatName> implements JsonSerialable{
    private CustomColors[] colors;

    private static final  HashMap<String, CharacterClass> ALL_CHARACTER_CLASSES = new HashMap<>();
    static{
        //guaranteed to have at least one character class
        addCharacterClass(new CharacterClass("Default", CustomColors.rainbow, 3, 3, 3, 3, 3));
    }
    
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
    
    public static void loadAll(){
		CharacterClass fire = new CharacterClass("Fire", CustomColors.fireColors, 2, 4, 5, 3, 3);
		CharacterClass air = new CharacterClass("Air", CustomColors.airColors, 2, 4, 3, 1, 5);
		CharacterClass earth = new CharacterClass("Earth", CustomColors.earthColors, 4, 1, 4, 4, 1);
		CharacterClass water = new CharacterClass("Water", CustomColors.waterColors, 5, 4, 1, 3, 3);
		
		addCharacterClasses(
            new CharacterClass[]{
                fire,
                air,
                earth,
                water
            }
        );
	}
    
    @Override
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
        ALL_CHARACTER_CLASSES.put(c.getName().toUpperCase(), c.copy());
    }
    public static void addCharacterClasses(CharacterClass[] c){
        for(CharacterClass cs : c){
            addCharacterClass(cs);
        }
    }
    public static CharacterClass getCharacterClassByName(String n){
        if(!ALL_CHARACTER_CLASSES.containsKey(n.toUpperCase())){
            throw new NoSuchElementException("Character class with name " + n + " not found. Did you remember to call CharacterClass.addCharacterClass(...)?");
        }
        return ALL_CHARACTER_CLASSES.get(n.toUpperCase()).copy();
    }
    public static CharacterClass[] getAll(){
        CharacterClass[] ret = new CharacterClass[ALL_CHARACTER_CLASSES.size()];
        Collection<CharacterClass> values = ALL_CHARACTER_CLASSES.values();
        int i = 0;
        for(CharacterClass cc : values){
            ret[i] = cc;
            i++;
        }
        return ret;
    }
    public static String[] getAllNames(){
        String[] ret = new String[ALL_CHARACTER_CLASSES.size()];
        Set<String> keys = ALL_CHARACTER_CLASSES.keySet();
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
    @Override
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
    
    public static CharacterClass deserializeJson(JsonObject obj){
        return new CharacterClass(
            getNameFrom(obj),
            getColorsFrom(obj),
            getStatBaseFrom(obj, CharacterStatName.HP.toString()),
            getStatBaseFrom(obj, CharacterStatName.ENERGY.toString()),
            getStatBaseFrom(obj, CharacterStatName.DMG.toString()),
            getStatBaseFrom(obj, CharacterStatName.REDUCTION.toString()),
            getStatBaseFrom(obj, CharacterStatName.SPEED.toString())
        );
    }
    
    public static CustomColors[] getColorsFrom(JsonObject obj){
        if(!obj.containsKey("colors")){
            throw new JsonException("Json Object is missing key 'colors'");
        }
        JsonArray a = obj.getJsonArray("colors");
        int len = a.size();
        CustomColors[] ret = new CustomColors[len];
        for(int i = 0; i < len; i++){
            ret[i] = CustomColors.fromString(a.getString(i));
        }
        return ret;
    }
}
