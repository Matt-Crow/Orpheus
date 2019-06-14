package passives;

import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import serialization.JsonUtil;
import upgradables.UpgradableJsonUtil;

/**
 *
 * @author Matt
 */
public class PassiveJsonUtil {
    public static void saveAll(File f){
        JsonObject[] objs = Arrays.stream(AbstractPassive.getAll()).map((AbstractPassive p)->{
            return serializeJson(p);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
    public static JsonObject serializeJson(AbstractPassive p){
        JsonObject obj = UpgradableJsonUtil.serializeJson(p);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("passive type", p.getPassiveType().toString());
        b.add("targets user", p.getTargetsUser());
        return b.build();
    }
    
    public static PassiveType getPassiveTypeFrom(JsonObject obj){
        if(!obj.containsKey("passive type")){
            throw new JsonException("Json Object is missing key 'passive type'");
        }
        return PassiveType.fromString(obj.getString("passive type"));
    }
    public static boolean getTargetsUserFrom(JsonObject obj){
        if(!obj.containsKey("targets user")){
            throw new JsonException("JsonObject missing key 'targets user'");
        }
        return obj.getBoolean("targets user");
    }
    
    public static AbstractPassive deserializeJson(JsonObject obj){
        AbstractPassive ret = null;
        PassiveType type = getPassiveTypeFrom(obj);
        
        switch(type){
            case THRESHOLD:
                ret = ThresholdPassive.deserializeJson(obj);
                break;
            case ONMELEEHIT:
                ret = OnMeleeHitPassive.deserializeJson(obj);
                break;
            case ONHIT:
                ret = OnHitPassive.deserializeJson(obj);
                break;
            case ONBEHIT:
                ret = OnBeHitPassive.deserializeJson(obj);
                break;
            default:
                System.out.println("Abstract passive cannot deserialize " + type);
                break;
        }
        return ret;
    }
}
