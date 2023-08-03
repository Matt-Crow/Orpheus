package orpheus.core.champions;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * ChampionSpecification is to Champion
 * as Build is to AssembledBuild
 */
public class ChampionSpecification implements Specification {

    /**
     * A unique identifier which designates which Champion is specified.
     */
    private final String name;

    /**
     * a brief description of this champion
     */
    private final String description;

    /**
     * @param name unique identifier which designates which Champion is specified
     */
    public ChampionSpecification(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BuildOrChampion getType() {
        return BuildOrChampion.CHAMPION;
    }

    @Override
    public JsonObject doToJson() {
        var asJson = Json.createObjectBuilder()
            .add("name", name)
            .add("description", description)
            .build();
        return asJson;
    }
    
    @Override
    public String toString() {
        return String.format("Champion: %s", name);
    }

    @Override
    public ChampionSpecification copy() {
        return new ChampionSpecification(name, description);
    }
}
