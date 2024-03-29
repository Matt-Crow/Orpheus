package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * a simple collection of properties used by other graphs
 */
public class WorldOccupant implements GraphElement {
    
    private final int x;
    private final int y;
    private final int radius;

    public WorldOccupant(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
    
    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("x", x)
            .add("y", y)
            .add("radius", radius)
            .build();
    }
}
