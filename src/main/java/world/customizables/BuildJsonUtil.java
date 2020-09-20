package world.customizables;

import util.Settings;
import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonUtil;

/**
 *
 * @author Matt Crow
 */
public class BuildJsonUtil {
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(Settings.getDataSet().getAllBuilds()).map((Build b)->{
            return serializeJson(b);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);  
    }
    
    public static void loadFile(File f){
        Build b = null;
        DataSet ds = Settings.getDataSet();
        for(JsonObject obj : JsonUtil.readFromFile(f)){
            b = deserializeJson(obj);
            if(b != null){
                ds.addBuild(b);
            }
        }
    }
    
    public static JsonObject serializeJson(Build b){
        JsonObjectBuilder j = Json.createObjectBuilder();
        j.add("type", "build");
        j.add("name", b.getName());
        j.add("character class", b.getClassName());
        
        JsonArrayBuilder acts = Json.createArrayBuilder();
        for(String activeName : b.getActiveNames()){
            acts.add(activeName);
        }
        j.add("actives", acts.build());
        
        JsonArrayBuilder pass = Json.createArrayBuilder();
        for(String passName : b.getPassiveNames()){
            pass.add(passName);
        }
        j.add("passives", pass.build());
        
        return j.build();
    }
    
    private static String getNameFrom(JsonObject obj){
        JsonUtil.verify(obj, "name");
        return obj.getString("name");
    }
    private static String getCharacterClassFrom(JsonObject obj){
        JsonUtil.verify(obj, "character class");
        return obj.getString("character class");
    }
    private static String[] getActivesFrom(JsonObject obj){
        JsonUtil.verify(obj, "actives");
        JsonArray a = obj.getJsonArray("actives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
    }
    private static String[] getPassivesFrom(JsonObject obj){
        JsonUtil.verify(obj, "passives");
        JsonArray a = obj.getJsonArray("passives");
        int len = a.size();
        String[] ret = new String[len];
        for(int i = 0; i < len; i++){
            ret[i] = a.getString(i);
        }
        return ret;
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
}
