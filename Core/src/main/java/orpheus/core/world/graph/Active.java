package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

public class Active implements GraphElement {

    private final String name;
    private final int cooldown;

    public Active(String name, int cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void draw(Graphics g) {
        
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("name", name)
            .add("cooldown", cooldown)
            .build();
    }

    public static Active fromJson(JsonObject json) {
        return new Active(
            json.getString("name"),
            json.getInt("cooldown")
        );
    }
}
