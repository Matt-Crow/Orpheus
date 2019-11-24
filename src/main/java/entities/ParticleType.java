package entities;

public enum ParticleType {
	BURST("burst"), 
    BEAM("beam"), 
    SHEAR("shear"),
    BLADE("blade"), 
    NONE("none");
    
    private final String name;
    
    ParticleType(String n){
        name = n;
    }
    
    public static ParticleType fromString(String s){
        ParticleType ret = null;
        for(ParticleType pt : values()){
            if(pt.toString().toLowerCase().equals(s.toLowerCase())){
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
};
