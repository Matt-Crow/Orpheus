package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * a simple collection of properties used by other graphs
 */
public class Entity implements GraphElement {
    
    private final int x;
    private final int y;
    private final int radius;

    public Entity(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("x", x)
            .add("y", y)
            .add("radius", radius)
            .build();
    }

    public Entity fromJson(JsonObject json) {
        return new Entity(
            json.getInt("x"),
            json.getInt("y"),
            json.getInt("radius")
        );
    }
}
