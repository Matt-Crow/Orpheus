package customizables.characterClass;

import java.util.NoSuchElementException;

public enum CharacterStatName {
	HP("hit points", 2000),
	ENERGY("energy", 50),
	DMG("damage dealt modifier", 1.0),
	REDUCTION("damage taken modifier", 1.0),
	SPEED("speed", 1.0);
    
    private final String name;
    private final double defaultValue;
    CharacterStatName(String n, double base){
        name = n;
        defaultValue = base;
    }
    
    public final double getDefaultValue(){
        return defaultValue;
    }
    
    public static CharacterStatName fromString(String s){
        CharacterStatName ret = null;
        for(CharacterStatName csn : values()){
            if(csn.toString().equalsIgnoreCase(s)){
                ret = csn;
                break;
            }
        }
        if(ret == null){
            throw new NoSuchElementException("There are no character stats called " + s);
        }
        return ret;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
