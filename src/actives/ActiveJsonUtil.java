package actives;

import java.io.File;
import java.util.Arrays;
import javax.json.JsonObject;
import serialization.JsonTest;

/**
 * Manages JSON representation of AbstractActives
 * @author Matt Crow
 */
public class ActiveJsonUtil{

    /*
    public static JsonObject serializeJson(AbstractActive act) {
        JsonObject obj = UpgradableJsonUtil.serializeJson(act);
        //javax jsonObjects are immutable
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", type.toString());
        b.add("particle type", particleType.toString());
        JsonArrayBuilder a = Json.createArrayBuilder();
        tags.forEach((t) -> {
            a.add(t.toString());
        });
        b.add("tags", a.build());
        return b.build();
    }*/
    
    public static void saveAll(File f){
        /*
        JsonObject[] objs = Arrays.stream(AbstractActive.getAll()).map((AbstractActive a)->{
            return serializeJson(a);
        }).toArray(size -> new JsonObject[size]);
        JsonTest.writeToFile(objs, f);*/
    }
}
