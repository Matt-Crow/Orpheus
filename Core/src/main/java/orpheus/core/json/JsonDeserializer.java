package orpheus.core.json;

import javax.json.JsonObject;
import serialization.JsonSerialable;

/**
 * Inverse of JsonSerialable
 */
public interface JsonDeserializer<T extends JsonSerialable> {
    
    /**
     * Inverse of T::toJson.
     * @param json the result of T::toJson
     * @throws Exception if json cannot be deserialized
     * @return a copy of the original T on which toJson was called
     */
    public T fromJson(JsonObject json);
}
