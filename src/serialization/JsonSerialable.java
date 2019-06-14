package serialization;

import javax.json.JsonObject;

/**
 * This is an alternative to making static helper classes
 * (A La UpgradableJsonUtil) for serializing objects as JSON.
 * As I say in UpgradableJsonUtil, I'm not sure which way I want to serialize
 * (in class or in a helper class), so I'm trying both for now.
 * @author Matt Crow
 */

/**
 * Classes which implement this interface
 * must include a static method to 'undo' the process
 * of serializeJson: a way to convert a JsonObject to 
 * an object of the same type as the class it was serialized from
 * @author Matt Crow
 */
public interface JsonSerialable {
    /**
     * Implementing classes should override this method
     * to insert at least everything that is passed to their
     * constructor to a JsonObject, as they will be reconstructed
     * from that JsonObject.
     * @return a JSON representation of this
     */
    public abstract JsonObject serializeJson();
}
