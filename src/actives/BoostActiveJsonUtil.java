package actives;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import upgradables.UpgradableJsonUtil;

/**
 * well this is useless
 * @author Matt Crow
 */
public class BoostActiveJsonUtil{
    public static JsonObject serializeJson(BoostActive ba){
        JsonObject obj = ActiveJsonUtil.serializeJson(ba);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static BoostActive deserializeJson(JsonObject obj){
        BoostActive ret = new BoostActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatusTableFrom(obj)
        );
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        return ret;
    }
}
