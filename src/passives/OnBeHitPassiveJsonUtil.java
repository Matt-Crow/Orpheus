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
public class OnBeHitPassiveJsonUtil {
    public static JsonObject serializeJson(OnBeHitPassive obhp){
        JsonObject obj = PassiveJsonUtil.serializeJson(obhp);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static OnBeHitPassive deserializeJson(JsonObject obj){
        OnBeHitPassive obh = new OnBeHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            PassiveJsonUtil.getTargetsUserFrom(obj)
        );
        obh.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return obh;
    }
}
