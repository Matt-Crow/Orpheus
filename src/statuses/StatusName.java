package statuses;

public enum StatusName {
	CHARGE("charge"),
	REGENERATION("regeneration"),
	RESISTANCE("resistance"),
	RUSH("rush"),
	STRENGTH("strength"),
	STUN("stun");
    
    private final String name;
    StatusName(String n){
        name = n;
    }
    public static StatusName fromName(String name){
        StatusName ret = null;
        for(StatusName sn : values()){
            if(sn.toString().toLowerCase().equals(name.toLowerCase())){
                ret = sn;
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