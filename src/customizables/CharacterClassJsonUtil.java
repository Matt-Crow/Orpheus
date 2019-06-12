package customizables;

import graphics.CustomColors;
import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import serialization.JsonUtil;
import upgradables.UpgradableJsonUtil;

/**
 *
 * @author Matt
 */
public class CharacterClassJsonUtil {
    public static void saveAll(File f){
        JsonObject[] objs = Arrays.stream(CharacterClass.getAll()).map((CharacterClass c)->{
            return serializeJson(c);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
    public static JsonObject serializeJson(CharacterClass cc){
        JsonObject obj = UpgradableJsonUtil.serializeJson(cc);
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        JsonArrayBuilder cols = Json.createArrayBuilder();
        for(CustomColors c : cc.getColors()){
            cols.add(c.toString());
        }
        b.add("type", "character class");
        b.add("colors", cols.build());
        return b.build();
    }
    
    public static CharacterClass deserializeJson(JsonObject obj){
        return new CharacterClass(
            UpgradableJsonUtil.getNameFrom(obj),
            getColorsFrom(obj),
            UpgradableJsonUtil.getStatBaseFrom(obj, CharacterStatName.HP),
            UpgradableJsonUtil.getStatBaseFrom(obj, CharacterStatName.ENERGY),
            UpgradableJsonUtil.getStatBaseFrom(obj, CharacterStatName.DMG),
            UpgradableJsonUtil.getStatBaseFrom(obj, CharacterStatName.REDUCTION),
            UpgradableJsonUtil.getStatBaseFrom(obj, CharacterStatName.SPEED)
        );
    }
    
    public static CustomColors[] getColorsFrom(JsonObject obj){
        if(!obj.containsKey("colors")){
            throw new JsonException("Json Object is missing key 'colors'");
        }
        JsonArray a = obj.getJsonArray("colors");
        int len = a.size();
        CustomColors[] ret = new CustomColors[len];
        for(int i = 0; i < len; i++){
            ret[i] = CustomColors.fromString(a.getString(i));
        }
        return ret;
    }
}
