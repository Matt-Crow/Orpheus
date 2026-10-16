package orpheus.core.champions;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.utils.Prototype;
import world.builds.Build;

/**
 * Used by players to specify which Build or Champion they want to play as.
 */
public interface Specification extends Prototype {
    
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

    public default JsonObject toJson() {
        var asJson = Json.createObjectBuilder(doToJson())
            .add("type", getType().toString())
            .build();
        return asJson;
    }

    public static Specification fromJson(JsonObject json) {
        var type = BuildOrChampion.fromString(json.getString("type"));
        switch (type) {
            case BUILD:
                return Build.fromJson(json);
            case CHAMPION:
                return new ChampionSpecification(json.getString("name"), json.getString("description"));
            default:
                throw new IllegalArgumentException(String.format("Unsupported type: %s", type));
        }
    }
}
