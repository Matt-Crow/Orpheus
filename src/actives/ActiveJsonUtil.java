package actives;

import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import serialization.JsonTest;
import upgradables.UpgradableJsonUtil;

/**
 * Manages JSON representation of AbstractActives
 * @author Matt Crow
 */
public class ActiveJsonUtil{

    
    public static JsonObject serializeJson(AbstractActive act) {
        JsonObject obj = UpgradableJsonUtil.serializeJson(act);
        //javax jsonObjects are immutable
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("active type", act.getActiveType().toString());
        b.add("particle type", act.getParticleType().toString());
        JsonArrayBuilder a = Json.createArrayBuilder();
        Arrays.stream(act.getTags()).forEach((t) -> {
            a.add(t.toString());
        });
        b.add("tags", a.build());
        return b.build();
    }
    public static ActiveType getActiveTypeFrom(JsonObject obj){
        if(!obj.containsKey("active type")){
            throw new JsonException("Json Object is missing key 'active type'");
        }
        return ActiveType.fromString(obj.getString("active type"));
    }
    
    public static void saveAll(File f){
        /*
        JsonObject[] objs = Arrays.stream(AbstractActive.getAll()).map((AbstractActive a)->{
            return serializeJson(a);
        }).toArray(size -> new JsonObject[size]);
        JsonTest.writeToFile(objs, f);*/
    }
}
