package customizables.actives;

public enum ActiveType {
	MELEE("melee active"),
	BOOST("boost active"),
	ELEMENTAL("elemental active");
    
    private final String name;
    ActiveType(String n){
        name = n;
    }
    
    public static ActiveType fromString(String name){
        ActiveType ret = null;
        for(ActiveType at : values()){
            if(at.toString().toLowerCase().equals(name.toLowerCase())){
                ret = at;
                break;
            }
        }
        return ret;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
