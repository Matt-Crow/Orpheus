package passives;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import upgradables.UpgradableJsonUtil;

/**
 *
 * @author Matt
 */
public class ThresholdPassiveJsonUtil {
    public static JsonObject serializeJson(ThresholdPassive tp){
        JsonObject obj = PassiveJsonUtil.serializeJson(tp);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static final ThresholdPassive deserializeJson(JsonObject obj){
        ThresholdPassive pass = new ThresholdPassive(
            UpgradableJsonUtil.getNameFrom(obj), 
            UpgradableJsonUtil.getStatBaseFrom(obj, PassiveStatName.THRESHOLD)
        );
        pass.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        
        return pass;
    }
}
