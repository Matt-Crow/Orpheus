package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

public class Orpheus implements GraphElement {

    private final int x;
    private final int y;
    private final int scrapMetal;

    public Orpheus(int x, int y, int scrapMetal) {
        this.x = x;
        this.y = y;
        this.scrapMetal = scrapMetal;
    }

    @Override
    public JsonObject toJson() {
        var json = Json.createObjectBuilder()
            .add("type", "Orpheus")
            .add("x", x)
            .add("y", y)
            .add("scrapMetal", scrapMetal)
            .build();
        return json;
    }

    @Override
    public void draw(Graphics g) {
        return;
    }
    
}
