package customizables;

import java.util.NoSuchElementException;

public enum CustomizableType {
	ACTIVE("active ability"),
	PASSIVE("passive ability"),
	CHARACTER_CLASS("character class");
    
    private final String name;
    CustomizableType(String n){
        name = n;
    }
    
    public static CustomizableType fromString(String s){
        CustomizableType ret = null;
        for(CustomizableType t : values()){
            if(t.toString().equalsIgnoreCase(s)){
                ret = t;
                break;
            }
        }
        if(ret == null){
            throw new NoSuchElementException("There is no UpgradableType named " + s);
        }
        return ret;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
