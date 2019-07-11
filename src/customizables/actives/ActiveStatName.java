package customizables.actives;

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
    
    @Override
    public String toString(){
        return displayValue;
    }
}
