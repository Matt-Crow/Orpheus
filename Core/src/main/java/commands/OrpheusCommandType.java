package commands;

/**
 *
 * @author Matt Crow
 */
public enum OrpheusCommandType {
    CHAT("chat"),
    WORLD_UPDATE("world update"),
    CONTROL_PRESSED("control pressed");
    
    private final String name;
    
    private OrpheusCommandType(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
