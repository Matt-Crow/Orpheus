package passives;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import upgradables.UpgradableJsonUtil;

/**
 *
 * @author Matt
 */
public class PassiveJsonUtil {
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
}
