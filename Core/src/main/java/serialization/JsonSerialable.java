package serialization;

import javax.json.JsonObject;

/**
 * @author Matt Crow
 * @see orpheus.core.json.JsonDeserializer
 */
public interface JsonSerialable {
    /**
     * Implementing classes should override this method
     * to insert at least everything that is passed to their
     * constructor to a JsonObject, as they will be reconstructed
     * from that JsonObject.
     * @return a JSON representation of this
     */
    public abstract JsonObject toJson();
}
