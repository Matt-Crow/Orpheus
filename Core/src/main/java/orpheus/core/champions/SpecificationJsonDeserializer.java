package orpheus.core.champions;

import javax.json.JsonObject;

import orpheus.core.json.JsonDeserializer;
import world.builds.BuildJsonUtil;

public class SpecificationJsonDeserializer implements JsonDeserializer<Specification> {

    @Override
    public Specification fromJson(JsonObject json) {
        var type = BuildOrChampion.fromString(json.getString("type"));
        switch (type) {
            case BUILD:
                return BuildJsonUtil.deserializeJson(json);
            case CHAMPION:
                return new ChampionSpecification(json.getString("name"));
            default:
                throw new IllegalArgumentException(String.format("Unsupported type: %s", type));
        }
    }
}
