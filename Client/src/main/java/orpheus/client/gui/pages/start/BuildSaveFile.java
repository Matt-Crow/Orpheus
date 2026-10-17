package orpheus.client.gui.pages.start;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import orpheus.core.utils.OrpheusJsonHelper;
import world.builds.Build;

public class BuildSaveFile {
    private final List<Build> builds;

    public BuildSaveFile(List<Build> builds) {
        this.builds = builds;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public JsonObject toJson() {
        var json = Json.createObjectBuilder()
            .add("builds", OrpheusJsonHelper.toJsonArrayOfObjects(
                builds
                    .stream()
                    .map(Build::toJson)
                    .toList()
            ))
            .build();
        return json;
    }

    public static BuildSaveFile fromJson(JsonObject json) {
        var builds = OrpheusJsonHelper.toListOfObjects(json.getJsonArray("builds"), Build::fromJson);
        return new BuildSaveFile(builds);
    }
}
