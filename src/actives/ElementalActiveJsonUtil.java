package actives;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import upgradables.UpgradableJsonUtil;

/**
 *
 * @author Matt
 */
public class ElementalActiveJsonUtil {
    public static JsonObject serializeJson(ElementalActive ea){
        JsonObject obj = ActiveJsonUtil.serializeJson(ea);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static ElementalActive deserializeJson(JsonObject obj){
        ElementalActive ret = new ElementalActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.ARC),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.RANGE),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.SPEED),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.AOE),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(ActiveJsonUtil.getParticleTypeFrom(obj));
        return ret;
    }
}
