package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

import gui.graphics.CustomColors;
import orpheus.core.world.graph.playables.Playable;

public class Orpheus implements Playable {

    private final int scrapMetal;
    private final double offsetAngle;

    public Orpheus(int scrapMetal, double offsetAngle) {
        this.scrapMetal = scrapMetal;
        this.offsetAngle = offsetAngle;
    }

    @Override
    public JsonObject toJson() {
        var json = Json.createObjectBuilder()
            .add("type", "Orpheus")
            .add("scrapMetal", scrapMetal)
            .add("offsetAngle", offsetAngle)
            .build();
        return json;
    }

    public static Orpheus fromJson(JsonObject json) {
        return new Orpheus(json.getInt("scrapMetal"), json.getJsonNumber("offsetAngle").doubleValue());
    }

    @Override
    public void draw(Graphics g) {
        return;
    }

    @Override
    public void drawAt(Graphics g, int x, int y) {
        
        if (scrapMetal == 0) {
            return;
        }

        var orbitRadius = Tile.TILE_SIZE;
        var r = Tile.TILE_SIZE / 2;
        g.setColor(CustomColors.DARK_GREY);
        var angleBetween = 360.0 / scrapMetal;
        for (var i = 0; i < scrapMetal; i++) {
            var angle = (offsetAngle + i * angleBetween) * Math.PI / 180;
            g.fillOval(
                (int)(x - r/2 + orbitRadius * Math.cos(angle)), 
                (int)(y - r/2 + orbitRadius * Math.sin(angle)), 
                r, 
                r
            );
        }
    }
    
}
