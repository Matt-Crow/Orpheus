package upgradables;

import java.util.NoSuchElementException;

public enum UpgradableType {
	ACTIVE("active ability"),
	PASSIVE("passive ability"),
	CHARACTER_CLASS("character class");
    
    private final String name;
    UpgradableType(String n){
        name = n;
    }
    
    public static UpgradableType fromString(String s){
        UpgradableType ret = null;
        for(UpgradableType t : values()){
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
