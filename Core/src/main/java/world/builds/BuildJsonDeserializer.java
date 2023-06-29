package world.builds;

import javax.json.JsonObject;

import orpheus.core.json.JsonDeserializer;
import serialization.JsonUtil;

public class BuildJsonDeserializer implements JsonDeserializer<Build> {

    @Override
    public Build fromJson(JsonObject json) {
        var actives = JsonUtil.toList(json.getJsonArray("actives"));
        var passives = JsonUtil.toList(json.getJsonArray("passives"));
        return new Build(
            json.getString("name"), 
            json.getString("character class"),
            actives.get(0),
            actives.get(1),
            actives.get(2),
            passives.get(0),
            passives.get(1),
            passives.get(2)
        ); 
    }
}
