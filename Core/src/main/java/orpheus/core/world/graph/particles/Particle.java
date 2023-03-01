package orpheus.core.world.graph.particles;

import java.awt.Color;
import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.world.graph.Entity;

/**
 * serializes and renders a particle
 */
public class Particle extends Entity {

    private final Color color;

    public Particle(int x, int y, int radius, Color color) {
        super(x, y, radius);
        this.color = color;
    }

    public void move() {
        // todo
    }

    @Override
    public void draw(Graphics g) {
        int r = getRadius();
        g.setColor(color);
        g.fillRect(getX() - r, getY() - r, r * 2, r * 2);
    }
    
    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder(super.serializeJson())
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
