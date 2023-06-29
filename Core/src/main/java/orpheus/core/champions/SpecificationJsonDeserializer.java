package orpheus.core.champions;

import javax.json.JsonObject;

import orpheus.core.json.JsonDeserializer;
import world.builds.BuildJsonDeserializer;

public class SpecificationJsonDeserializer implements JsonDeserializer<Specification> {

    private final BuildJsonDeserializer buildDeserializer = new BuildJsonDeserializer();

    @Override
    public Specification fromJson(JsonObject json) {
        var type = BuildOrChampion.fromString(json.getString("type"));
        switch (type) {
            case BUILD:
                return buildDeserializer.fromJson(json);
            case CHAMPION:
                return new ChampionSpecification(json.getString("name"));
            default:
                throw new IllegalArgumentException(String.format("Unsupported type: %s", type));
        }
    }
}
