package orpheus.core.world.graph;

import java.awt.Color;
import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * serializes and renders a particle
 */
public class Particle implements GraphElement {

    private final int x;
    private final int y;
    private final int radius;
    private final Color color;

    public Particle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        int r = radius;
        g.setColor(color);
        g.fillRect(x - r, y - r, r * 2, r * 2);
    }
    
    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("x", x)
            .add("y", y)
            .add("radius", radius)
            .add("color", color.getRGB())
            .build();
    }

    public static Particle fromJson(JsonObject json) {
        return new Particle(
            json.getInt("x"),
            json.getInt("y"),
            json.getInt("radius"),
            new Color(json.getInt("color"))
        );
    }
}
