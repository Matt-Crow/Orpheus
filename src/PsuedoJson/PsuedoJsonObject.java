package PsuedoJson;

import java.util.HashMap;
import static java.lang.System.out;

public class PsuedoJsonObject {
    private final String name;
    private final HashMap<String, String> pairs;
    private final HashMap<String, PsuedoJsonObject> subObjects; 
    //until I figure out how to incorperate into pairs
    
    public PsuedoJsonObject(String s){
        name = s;
        pairs = new HashMap<>();
        subObjects = new HashMap<>();
    }
    
    public void addPair(String key, String value){
        pairs.put(key, value);
    }
    public void addPair(String key, PsuedoJsonObject obj){
        subObjects.put(key, obj);
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("{").append(System.lineSeparator());
        
        pairs.keySet().stream().forEach(
            key -> sb.append("    ")
                .append(key)
                .append(" : ")
                .append(pairs.get(key))
            .append(System.lineSeparator())
        );
        subObjects.keySet().stream().forEach(
            key -> sb.append("    ")
                .append(key)
                .append(" : ")
                .append(subObjects.get(key).toString())
            .append(System.lineSeparator())
            );
        
        
        sb.append("}");
        return sb.toString();
    }
    
    public void displayData(){
        out.println(toString());
    }
}
