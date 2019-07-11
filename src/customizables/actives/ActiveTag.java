package customizables.actives;

public enum ActiveTag {
    KNOCKSBACK("knocks back");
    
    private final String name;
    ActiveTag(String n){
        name = n;
    }
    
    public static ActiveTag fromString(String s){
        ActiveTag ret = null;
        for(ActiveTag tag : values()){
            if(tag.toString().toLowerCase().equals(s.toLowerCase())){
                ret = tag;
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
