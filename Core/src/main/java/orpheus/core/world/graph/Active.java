package orpheus.core.world.graph;

import java.awt.Graphics;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.utils.OrpheusJsonHelper;

public class Active implements GraphElement {

    private final String name;
    private final List<String> unavailabilityMessages;

    public Active(String name, int cooldown) {
        this(name, List.of(String.format("On cooldown %3d", cooldown)));
    }

    public Active(String name, List<String> unavailabilityMessages) {
        this.name = name;
        this.unavailabilityMessages = unavailabilityMessages;
    }

    public String getName() {
        return name;
    }

    public List<String> getUnavailabilityMessages() {
        return unavailabilityMessages;
    }

    public boolean isAvailable() {
        return unavailabilityMessages.isEmpty();
    }

    @Override
    public void draw(Graphics g) {
        
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("name", name)
            .add("unavailabilityMessages", OrpheusJsonHelper.toJsonArrayOfStrings(unavailabilityMessages))
            .build();
    }

    public static Active fromJson(JsonObject json) {
        return new Active(
            json.getString("name"),
            OrpheusJsonHelper.toListOfStrings(json.getJsonArray("unavailabilityMessages"))
        );
    }
}
