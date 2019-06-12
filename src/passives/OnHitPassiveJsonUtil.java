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
public class OnHitPassiveJsonUtil {
    public static JsonObject serializeJson(OnHitPassive ohp){
        JsonObject obj = PassiveJsonUtil.serializeJson(ohp);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static OnHitPassive deserializeJson(JsonObject obj){
        OnHitPassive ret = new OnHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            PassiveJsonUtil.getTargetsUserFrom(obj)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return ret;
    }
}
