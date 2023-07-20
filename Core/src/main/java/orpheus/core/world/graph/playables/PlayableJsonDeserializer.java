package orpheus.core.world.graph.playables;

import javax.json.JsonObject;

import orpheus.core.world.graph.Orpheus;

/**
 * deserializes Playables which have been serialized to JSON
 */
public class PlayableJsonDeserializer {
    
    public static Playable fromJson(JsonObject json) {
        var type = json.getString("type");
        switch (type) {
            case "AssembledBuild":
                return new AssembledBuild();
            case "Orpheus":
                return Orpheus.fromJson(json);
        }
        throw new IllegalArgumentException(String.format("Unsupported type: \"%s\"", type));
    }
}
