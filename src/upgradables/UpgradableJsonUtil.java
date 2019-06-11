package upgradables;

import actives.AbstractActive;
import customizables.CharacterClass;
import java.io.File;
import java.util.function.BiConsumer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import passives.AbstractPassive;
import serialization.JsonTest;
import static upgradables.AbstractUpgradable.deserializeJson;

/**
 *
 * @author Matt Crow
 */
public class UpgradableJsonUtil {
    
    /**
     * Imports the JSON-serialized 
     * upgradables from the given file
     * into the program
     * @param f the file to import
     */
    public static void loadFile(File f){
        AbstractUpgradable au = null;
        for(JsonObject obj : JsonTest.readFromFile(f)){
            au = deserializeJson(obj);
            if(au != null){
                if(au instanceof AbstractPassive){
                    AbstractPassive.addPassive((AbstractPassive)au);
                } else if(au instanceof AbstractActive){
                    AbstractActive.addActive((AbstractActive)au);
                } else if(au instanceof CharacterClass){
                    CharacterClass.addCharacterClass((CharacterClass)au);
                } else {
                    System.out.println("Couldn't deserialize " + au.getClass().getName());
                    JsonTest.pprint(obj, 0);
                }
            }
        }
    }
    
    public void a(Enum e){
        e.toString();
    }
    
    public static JsonObject serializeJson(AbstractUpgradable au){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("upgradable type", au.upgradableType.toString());
        b.add("name", au.getName());
        b.add("status table", au.getInflict().serializeJson());
        
        /*
        JsonObjectBuilder statsJson = Json.createObjectBuilder();
        
        BiConsumer bi = (Enum key, Integer value)->{
            statsJson.add(key.toString(), value);
        }; 
        au.getBases()
            .forEach(
                bi);
        b.add("stats", statsJson.build());
        */
        return b.build();
    }
}
