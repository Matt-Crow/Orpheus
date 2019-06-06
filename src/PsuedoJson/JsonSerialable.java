package PsuedoJson;

import javax.json.JsonObject;

/**
 *
 * @author Matt
 */
public interface JsonSerialable {
    public abstract JsonObject serializeJson();
    public static Object deserializeJson(JsonObject obj){
        throw new UnsupportedOperationException();
    }
}
