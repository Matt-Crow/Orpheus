package orpheus.core.champions;

import javax.json.Json;
import javax.json.JsonObject;

import serialization.JsonSerialable;

/**
 * Used by players to specify which Build or Champion they want to play as.
 */
public interface Specification extends JsonSerialable {
    
    /**
     * @return a unique identifier for this
     */
    public String getName();

    /**
     * @return a brief description of what this specifies
     */
    public String getDescription();

    /**
     * @return the type of this specification. Used to determine how ti resolve
     *  this to an AssembledBuild or Champion.
     */
    public BuildOrChampion getType();

    /**
     * Subclasses should override this method to return their properties,
     * excluding those of Specification.
     * It should not be called directly!
     * @return this object, as JSON
     */
    public JsonObject doToJson();

    @Override
    public default JsonObject toJson() {
        var asJson = Json.createObjectBuilder(doToJson())
            .add("type", getType().toString())
            .build();
        return asJson;
    }
}
