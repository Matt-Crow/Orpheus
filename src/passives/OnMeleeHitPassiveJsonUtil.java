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
public class OnMeleeHitPassiveJsonUtil {
    public static JsonObject serializeJson(OnMeleeHitPassive omhp){
        JsonObject obj = PassiveJsonUtil.serializeJson(omhp);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    public static OnMeleeHitPassive deserializeJson(JsonObject obj){
        OnMeleeHitPassive pass = new OnMeleeHitPassive(
            UpgradableJsonUtil.getNameFrom(obj),
            PassiveJsonUtil.getTargetsUserFrom(obj)
        );
        pass.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        return pass;
    }
}
