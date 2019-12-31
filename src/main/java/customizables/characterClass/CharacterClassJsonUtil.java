package customizables.characterClass;

import customizables.CustomizableJsonUtil;
import graphics.CustomColors;
import java.io.File;
import java.util.Arrays;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import serialization.JsonUtil;

/**
 *
 * @author Matt Crow
 */
public class CharacterClassJsonUtil {
    public static void saveAllToFile(File f){
        JsonObject[] objs = Arrays.stream(CharacterClass.getAll()).map((CharacterClass c)->{
            return serializeJson(c);
        }).toArray(size -> new JsonObject[size]);
        JsonUtil.writeToFile(objs, f);
    }
    
    public static JsonObject serializeJson(CharacterClass cc){
        JsonObject obj = CustomizableJsonUtil.serializeJson(cc);
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
            CustomizableJsonUtil.getNameFrom(obj),
            getColorsFrom(obj),
            CustomizableJsonUtil.getStatBaseFrom(obj, CharacterStatName.HP),
            CustomizableJsonUtil.getStatBaseFrom(obj, CharacterStatName.ENERGY),
            CustomizableJsonUtil.getStatBaseFrom(obj, CharacterStatName.DMG),
            CustomizableJsonUtil.getStatBaseFrom(obj, CharacterStatName.REDUCTION),
            CustomizableJsonUtil.getStatBaseFrom(obj, CharacterStatName.SPEED)
        );
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
}
