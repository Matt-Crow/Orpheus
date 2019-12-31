package customizables.actives;

import controllers.Master;
import customizables.CustomizableJsonUtil;
import entities.ParticleType;
import graphics.CustomColors;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;
import serialization.JsonUtil;

/**
 * This class serves to handle all
 * of the Json serialization for
 * active abilities.
 * 
 * @author Matt Crow
 */
public final class ActiveJsonUtil {
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(Master.getDataSet().getAllActives()).map((AbstractActive a)->{
            return serializeJson(a);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
    public static JsonObject serializeJson(AbstractActive aa) {
        JsonObjectBuilder b = JsonUtil.deconstruct(CustomizableJsonUtil.serializeJson(aa));
        b.add("active type", aa.getActiveType().toString());
        b.add("particle type", aa.getParticleType().toString());
        JsonArrayBuilder cols = Json.createArrayBuilder();
        for(CustomColors c : aa.getColors()){
            cols.add(c.toString());
        }
        b.add("colors", cols.build());
        JsonArrayBuilder a = Json.createArrayBuilder();
        for(ActiveTag t : aa.getTags()){
            a.add(t.toString());
        };
        b.add("tags", a.build());
        return b.build();
    }
    
    public static ActiveType getActiveTypeFrom(JsonObject obj){
        JsonUtil.verify(obj, "active type");
        return ActiveType.fromString(obj.getString("active type"));
    }
    public static ParticleType getParticleTypeFrom(JsonObject obj){
        JsonUtil.verify(obj, "particle type");
        return ParticleType.fromString(obj.getString("particle type"));
    }
    public static CustomColors[] getColorsFrom(JsonObject obj){
        JsonUtil.verify(obj, "colors");
        JsonArray a = obj.getJsonArray("colors");
        int len = a.size();
        CustomColors[] ret = new CustomColors[len];
        for(int i = 0; i < len; i++){
            ret[i] = CustomColors.fromString(a.getString(i));
        }
        return ret;
    }
    public static ArrayList<ActiveTag> getTagsFrom(JsonObject obj){
        JsonUtil.verify(obj, "tags");
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
    
    public static MeleeActive deserializeMeleeActive(JsonObject obj){
        MeleeActive ret = new MeleeActive(
            CustomizableJsonUtil.getNameFrom(obj),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(ActiveJsonUtil.getParticleTypeFrom(obj));
        return ret;
    }
    public static ElementalActive deserializeElementalActive(JsonObject obj){
        ElementalActive ret = new ElementalActive(
            CustomizableJsonUtil.getNameFrom(obj),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.ARC),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.RANGE),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.SPEED),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.AOE),
            CustomizableJsonUtil.getStatBaseFrom(obj, ActiveStatName.DAMAGE)
        );
        ret.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        ret.setParticleType(ActiveJsonUtil.getParticleTypeFrom(obj));
        return ret;
    }
    public static BoostActive deserializeBoostActive(JsonObject obj){
        BoostActive ret = new BoostActive(
            CustomizableJsonUtil.getNameFrom(obj),
            CustomizableJsonUtil.getStatusTableFrom(obj)
        );
        ActiveJsonUtil.getTagsFrom(obj).stream().forEach(t->ret.addTag(t));
        return ret;
    }
    
    public static AbstractActive deserializeJson(JsonObject obj){
        AbstractActive ret = null;
        ActiveType type = getActiveTypeFrom(obj);
        
        switch(type){
            case MELEE:
                ret = deserializeMeleeActive(obj);
                break;
            case BOOST:
                ret = deserializeBoostActive(obj);
                break;
            case ELEMENTAL:
                ret = deserializeElementalActive(obj);
                break;
            default:
                System.out.println("Abstract active cannot deserialize " + obj.getString("type"));
                break;
        }
        if(ret != null){
            ret.setColors(getColorsFrom(obj));
        }
        return ret;
    }
}
