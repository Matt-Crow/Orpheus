package customizables.characterClass;

import java.util.*;

import customizables.AbstractCustomizable;
import graphics.CustomColors;
import customizables.CustomizableType;
import java.text.DecimalFormat;
import util.Number;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractCustomizable{
    private CustomColors[] colors;
    public static final int BASE_HP = 2000;

    private static final  HashMap<String, CharacterClass> ALL = new HashMap<>();
    static{
        //guaranteed to have at least one character class
        addCharacterClass(new CharacterClass("Default", CustomColors.rainbow, 3, 3, 3, 3, 3));
    }
    
    // initializers
    public CharacterClass(String n, CustomColors[] cs, int HP, int energy, int dmg, int reduction, int speed){
        super(CustomizableType.CHARACTER_CLASS, n);
        colors = cs;

        setStat(CharacterStatName.HP, HP);
        setStat(CharacterStatName.ENERGY, energy);
        setStat(CharacterStatName.DMG, dmg);
        setStat(CharacterStatName.REDUCTION, reduction);
        setStat(CharacterStatName.SPEED, speed);
    }
    
    public static void loadAll(){
		CharacterClass fire = new CharacterClass("Fire", CustomColors.fireColors, 1, 4, 5, 4, 3);
		CharacterClass air = new CharacterClass("Air", CustomColors.airColors, 2, 5, 3, 1, 5);
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
        ALL.put(c.getName().toUpperCase(), c.copy());
    }
    public static void addCharacterClasses(CharacterClass[] c){
        for(CharacterClass cs : c){
            addCharacterClass(cs);
        }
    }
    public static CharacterClass getCharacterClassByName(String n){
        if(!ALL.containsKey(n.toUpperCase())){
            throw new NoSuchElementException("Character class with name " + n + " not found. Did you remember to call CharacterClass.addCharacterClass(...)?");
        }
        return ALL.get(n.toUpperCase()).copy();
    }
    public static CharacterClass[] getAll(){
        CharacterClass[] ret = new CharacterClass[ALL.size()];
        Collection<CharacterClass> values = ALL.values();
        int i = 0;
        for(CharacterClass cc : values){
            ret[i] = cc;
            i++;
        }
        return ret;
    }
    public static String[] getAllNames(){
        String[] ret = new String[ALL.size()];
        Set<String> keys = ALL.keySet();
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
        
        /*
        value | multiplier
        ------------------
        1     | 80%
        2     | 90%
        3     | 100%
        4     | 110%
        5     | 120%
        */
        addStat(c, value, c.getDefaultValue() * (1 + 0.1 * (value - 3)));
    }
    @Override
    public String getDescription(){
        return getName() + ": \n" 
                        + "(*) Maximum hit points: " + (int)getStatValue(CharacterStatName.HP) + "\n"
                        + "(*) Maximum energy: " + new DecimalFormat("##.#").format(getStatValue(CharacterStatName.ENERGY)) + "\n"
                        + "(*) Damage dealt modifier: " + (int)(getStatValue(CharacterStatName.DMG) * 100) + "%\n"
                        + "(*) Damage taken modifier: " + (int)(getStatValue(CharacterStatName.REDUCTION) * 100) + "%\n"
                        + "(*) Movement speed modifier: " + (int)(getStatValue(CharacterStatName.SPEED) * 100) + "%\n";
    }
}
