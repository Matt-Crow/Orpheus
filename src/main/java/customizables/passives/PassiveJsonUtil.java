package customizables.passives;

import controllers.Master;
import customizables.CustomizableJsonUtil;
import java.io.File;
import java.util.Arrays;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonUtil;

/**
 * This class is used to manage all
 * of the JSON serialization used by
 * passive abilities.
 * 
 * @author Matt Crow
 */
public class PassiveJsonUtil {
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(Master.getDataSet().getAllPassives()).map((AbstractPassive p)->{
            return serializeJson(p);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
    public static JsonObject serializeJson(AbstractPassive ap){
        JsonObjectBuilder b = JsonUtil.deconstruct(CustomizableJsonUtil.serializeJson(ap));
        b.add("passive type", ap.getPassiveType().toString());
        b.add("targets user", ap.getTargetsUser());
        return b.build();
    }
    
    public static PassiveType getPassiveTypeFrom(JsonObject obj){
        JsonUtil.verify(obj, "passive type");
        return PassiveType.fromString(obj.getString("passive type"));
    }
    public static boolean getTargetsUserFrom(JsonObject obj){
        JsonUtil.verify(obj, "targets user");
        return obj.getBoolean("targets user");
    }
    
    public static OnBeHitPassive deserializeOnBeHit(JsonObject obj){
        OnBeHitPassive obh = new OnBeHitPassive(
            CustomizableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        obh.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        return obh;
    }
    
    public static OnHitPassive deserializeOnHit(JsonObject obj){
        OnHitPassive ret = new OnHitPassive(
            CustomizableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        ret.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        return ret;
    }
    
    public static OnMeleeHitPassive deserializeOnMeleeHit(JsonObject obj){
        OnMeleeHitPassive pass = new OnMeleeHitPassive(
            CustomizableJsonUtil.getNameFrom(obj),
            getTargetsUserFrom(obj)
        );
        pass.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        return pass;
    }
    
    public static ThresholdPassive deserializeThreshold(JsonObject obj){
        ThresholdPassive pass = new ThresholdPassive(
            CustomizableJsonUtil.getNameFrom(obj), 
            CustomizableJsonUtil.getStatBaseFrom(obj, PassiveStatName.THRESHOLD)
        );
        pass.setInflict(CustomizableJsonUtil.getStatusTableFrom(obj));
        
        return pass;
    }
    
    public static AbstractPassive deserializeJson(JsonObject obj){
        AbstractPassive ret = null;
        PassiveType type = getPassiveTypeFrom(obj);
        
        switch(type){
            case THRESHOLD:
                ret = deserializeThreshold(obj);
                break;
            case ONMELEEHIT:
                ret = deserializeOnMeleeHit(obj);
                break;
            case ONHIT:
                ret = deserializeOnHit(obj);
                break;
            case ONBEHIT:
                ret = deserializeOnBeHit(obj);
                break;
            default:
                throw new UnsupportedOperationException("Abstract passive cannot deserialize " + type);
        }
        return ret;
    }
}
