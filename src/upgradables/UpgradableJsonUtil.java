package upgradables;

import actives.AbstractActive;
import actives.ActiveJsonUtil;
import customizables.CharacterClass;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import passives.AbstractPassive;
import passives.PassiveJsonUtil;
import serialization.JsonTest;
import statuses.StatusTable;

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
    
    
    public static JsonObject serializeJson(AbstractUpgradable au){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("upgradable type", au.upgradableType.toString());
        b.add("name", au.getName());
        b.add("status table", au.getInflict().serializeJson());
        
        JsonObjectBuilder statsJson = Json.createObjectBuilder();
        BiConsumer<Enum, Integer> bi = (Enum key, Integer value)->{
            statsJson.add(key.toString(), value);
        }; 
        au.getBases()
            .forEach(
                bi);
        b.add("stats", statsJson.build());
        
        return b.build();
    }
    
    //make method in JsonUtil or annotation
    public static UpgradableType getUpgradableTypeFrom(JsonObject obj){
        if(!obj.containsKey("upgradable type")){
            throw new JsonException("Json Object is missing key 'upgradable type'");
        }
        return UpgradableType.fromString(obj.getString("upgradable type"));
    }
    public static String getNameFrom(JsonObject obj){
        if(!obj.containsKey("name")){
            throw new JsonException("Json Object is missing key 'name'");
        }
        return obj.getString("name");
    }
    public static StatusTable getStatusTableFrom(JsonObject obj){
        if(!obj.containsKey("status table")){
            throw new JsonException("Json Object is missing key 'status table'");
        }
        return StatusTable.deserializeJson(obj.getJsonObject("status table"));
    }
    public static int getStatBaseFrom(JsonObject obj, Enum statName){
        if(!obj.containsKey("stats")){
            throw new JsonException("Json Object is missing key 'stats'");
        }
        int ret = -1;
        JsonObject temp = obj.getJsonObject("stats");
        if(temp.containsKey(statName.toString())){
            ret = temp.getInt(statName.toString());
        } else {
            throw new JsonException("stats object is missing key " + statName);
        }
        
        return ret;
    }
    public static AbstractUpgradable deserializeJson(JsonObject obj){
        AbstractUpgradable ret = null;
        switch(getUpgradableTypeFrom(obj)){
            case ACTIVE:
                ret = ActiveJsonUtil.deserializeJson(obj);
                break;
            case PASSIVE:
                ret = PassiveJsonUtil.deserializeJson(obj);
                break;
            case CHARACTER_CLASS:
                ret = CharacterClass.deserializeJson(obj);
                break;
            default:
                throw new JsonException("Couldn't deserialize upgradable type " + getUpgradableTypeFrom(obj));
        }
        System.out.println(ret.getDescription());
        return ret;
    }
}
