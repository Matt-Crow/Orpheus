package PsuedoJson;

import java.util.HashMap;
import static java.lang.System.out;

/**
 * The PsuedoJsonObject is used to generate Json-like, textual representations of an object.
 * 
 * each PsuedoJsonObject contains a name, 
 * as well as any number of key-value pairs of String to String,
 * or String to PsuedoJsonObject pairs.
 * 
 * An example of a PsuedoJsonObject representation of an object would be this:
 * <pre>
 * ObjExample {
 *     key1 : value1
 *     key2 : value2
 *     key3 : PJO {
 *     pjoKey1 : value3
 *     }
 * }</pre>
 * 
 * Note that this is not the same as Json.
 * 
 * To create a PsuedoJsonObject, 
 * simply invoke the constructor, 
 * passing in the name you want it to have,
 * then manually add each pair, like so:
 * 
 * <pre>
 * PsuedoJsonObject test = new PsuedoJsonObject("Test");
 * test.addPair("key1", "value1");
 * test.addPair("key2", "value2");
 * 
 * PsuedoJsonObject sub = new PsuedoJsonObject("PJO");
 * sub.addPair("pjoKey1", "value3");
 * 
 * test.addPair("key3", sub);
 * </pre>
 */
public class PsuedoJsonObject {
    private final String name;
    private final HashMap<String, String> pairs; //string keys
    private final HashMap<String, PsuedoJsonObject> subObjects; //object keys 
    //until I figure out how to incorperate into pairs
    
    /**
     * 
     * @param s The name of the PsuedoJsonObject
     */
    public PsuedoJsonObject(String s){
        name = s;
        pairs = new HashMap<>();
        subObjects = new HashMap<>();
    }
    
    /**
     * Adds a key-value pair to this.
     * @param key The key of the pair. If key already exists in this' string keys, overrides the old one.
     * Note that this does not override any values in the object keys.
     * @param value The value to pair with the given key.
     */
    public void addPair(String key, String value){
        pairs.put(key, value);
    }
    
    /**
     * Adds a key-value pair to this.
     * @param key The key of the pair. If key already exists in this' object keys, overrides the old one.
     * Note that this does not override any values in the string keys.
     * @param obj The value to pair with the given key.
     */
    public void addPair(String key, PsuedoJsonObject obj){
        if(!obj.equals(this) && !recursiveCheck(obj) && !obj.recursiveCheck(this)){
            subObjects.put(key, obj);
        } else {
            out.println("Illegal object passed in as value: ");
            obj.displayData();
            out.println("Contains this as a value");
        }
    }
    
    /**
     * Recursively checks to make sure a PsuedoJsonObject does not contain another PJO in any of its sub-objects.
     * @param obj The PJO to check for.
     * @return Whether or not obj is connected to this.
     */
    public boolean recursiveCheck(PsuedoJsonObject obj){
        boolean keyExists = subObjects.values().contains(obj);
        
        if(!keyExists){
            for(PsuedoJsonObject pjo : subObjects.values()){
                keyExists = pjo.recursiveCheck(obj);
                if(keyExists){
                    break;
                }
            }
        }
        
        return keyExists;
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
