package customizables;

import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonTest;

/**
 *
 * @author Matt
 */
public class BuildJsonUtil {
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(Build.getAll()).map((Build b)->{
            return serializeJson(b);
        }).toArray(size -> new JsonObject[size]);
        JsonTest.writeToFile(objs, f);  
    }
    
    public static JsonObject serializeJson(Build build) {
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", "build");
        b.add("name", build.getName());
        b.add("character class", build.getClassName());
        
        JsonArrayBuilder acts = Json.createArrayBuilder();
        for(String activeName : build.getActiveNames()){
            acts.add(activeName);
        }
        b.add("actives", acts.build());
        
        JsonArrayBuilder pass = Json.createArrayBuilder();
        for(String passName : build.getPassiveNames()){
            pass.add(passName);
        }
        b.add("passives", pass.build());
        
        return b.build();
    }
    
    public static Build deserializeJson(JsonObject obj){
        String[] actives = getActivesFrom(obj);
        String[] passives = getPassivesFrom(obj);
        return new Build(
            getNameFrom(obj),
            getCharacterClassFrom(obj),
            actives[0],
            actives[1],
            actives[2],
            passives[0],
            passives[1],
            passives[2]
        );
    }
    
    private static String getNameFrom(JsonObject obj){
        if(!obj.containsKey("name")){
            throw new JsonException("Json Object is missing key 'name'");
        }
        return obj.getString("name");
    }
    private static String getCharacterClassFrom(JsonObject obj){
        if(!obj.containsKey("character class")){
            throw new JsonException("Json Object is missing key 'character class'");
        }
        return obj.getString("character class");
    }
    private static String[] getActivesFrom(JsonObject obj){
        if(!obj.containsKey("actives")){
            throw new JsonException("Json Object is missing key 'actives'");
        }
        
        JsonArray a = obj.getJsonArray("actives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
    }
    private static String[] getPassivesFrom(JsonObject obj){
        if(!obj.containsKey("passives")){
            throw new JsonException("Json Object is missing key 'passives'");
        }
        
        JsonArray a = obj.getJsonArray("passives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
    }
}
