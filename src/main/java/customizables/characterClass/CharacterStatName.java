package customizables.characterClass;

import java.util.NoSuchElementException;

public enum CharacterStatName {
	HP("hit points"),
	ENERGY("energy"),
	DMG("damage dealt modifier"),
	REDUCTION("damage taken modifier"),
	SPEED("speed");
    
    private final String name;
    CharacterStatName(String n){
        name = n;
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
