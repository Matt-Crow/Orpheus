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
public class MeleeActiveJsonUtil {
    public static JsonObject serializeJson(MeleeActive ma){
        JsonObject obj = ActiveJsonUtil.serializeJson(ma);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b.build();
    }
    
    public static final MeleeActive deserializeJson(JsonObject obj){
        MeleeActive ret = new MeleeActive(
            UpgradableJsonUtil.getNameFrom(obj),
            UpgradableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(UpgradableJsonUtil.getStatusTableFrom(obj));
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(ActiveJsonUtil.getParticleTypeFrom(obj));
        return ret;
    }
}
