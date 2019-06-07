package passives;

public enum PassiveType {
	ONBEHIT("on be hit passive"),
	ONHIT("on hit passive"),
	ONMELEEHIT("on melee hit passive"),
	THRESHOLD("threshold passive");
    
    private final String name;
    PassiveType(String n){
        name = n;
    }
    
    public static PassiveType fromString(String name){
        PassiveType ret = null;
        for(PassiveType pt : values()){
            if(pt.toString().toLowerCase().equals(name.toLowerCase())){
                ret = pt;
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
