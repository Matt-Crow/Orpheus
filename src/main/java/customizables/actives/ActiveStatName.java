package customizables.actives;

import java.util.NoSuchElementException;

public enum ActiveStatName{
	NAME("Name"),
    ARC("Arc"),
	RANGE("Range"),
	SPEED("Speed"),
	AOE("AOE"),
	DAMAGE("Damage"),
    PARTICLETYPE("Particle type"),
    TAGS("Tags");
    
    private final String displayValue;
    ActiveStatName(String text){
        displayValue = text;
    }
    
    public static ActiveStatName fromString(String s){
        ActiveStatName ret = null;
        for(ActiveStatName asn : values()){
            if(asn.toString().equalsIgnoreCase(s)){
                ret = asn;
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
        return displayValue;
    }
}
