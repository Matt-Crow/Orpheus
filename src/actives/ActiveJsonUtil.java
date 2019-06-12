package actives;

import entities.ParticleType;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import serialization.JsonUtil;
import upgradables.UpgradableJsonUtil;

/**
 * Manages JSON representation of AbstractActives
 * @author Matt Crow
 */
public class ActiveJsonUtil{
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(AbstractActive.getAll()).map((AbstractActive a)->{
            return serializeJson(a);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
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
    public static ParticleType getParticleTypeFrom(JsonObject obj){
        if(!obj.containsKey("particle type")){
            throw new JsonException("Json Object is missing key 'particle type'");
        }
        return ParticleType.fromString(obj.getString("particle type"));
    }
    public static ArrayList<ActiveTag> getTagsFrom(JsonObject obj){
        if(!obj.containsKey("tags")){
            throw new JsonException("Json Object missing key 'tags'");
        }
        ArrayList<ActiveTag> ret = new ArrayList<>();
        ActiveTag tag = null;
        for(JsonValue jv : obj.getJsonArray("tags")){
            if(jv.getValueType().equals(JsonValue.ValueType.STRING)){
                tag = ActiveTag.fromString(((JsonString)jv).getString());
                if(tag == null){
                    throw new NullPointerException("Unknown tag: " + jv);
                } else {
                    ret.add(tag);
                }
            }
        }
        return ret;
    }
    
    public static AbstractActive deserializeJson(JsonObject obj){
        AbstractActive ret = null;
        ActiveType type = getActiveTypeFrom(obj);
        
        switch(type){
            case MELEE:
                ret = MeleeActiveJsonUtil.deserializeJson(obj);
                break;
            case BOOST:
                ret = BoostActiveJsonUtil.deserializeJson(obj);
                break;
            case ELEMENTAL:
                ret = ElementalActiveJsonUtil.deserializeJson(obj);
                break;
            default:
                System.out.println("Abstract active cannot deserialize " + obj.getString("type"));
                break;
        }
        return ret;
    }
    
    public static void saveAll(File f){
        /*
        JsonObject[] objs = Arrays.stream(AbstractActive.getAll()).map((AbstractActive a)->{
            return serializeJson(a);
        }).toArray(size -> new JsonObject[size]);
        JsonTest.writeToFile(objs, f);*/
    }
}
