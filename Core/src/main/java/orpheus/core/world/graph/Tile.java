package orpheus.core.world.graph;

import java.awt.Color;
import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * template for drawing tiles in a Map
 */
public class Tile implements GraphElement {
    public static final int TILE_SIZE = 100;
    private static final int TILE_SPACING = (int)(TILE_SIZE * 0.05);
    private static final int RECT_SIZE = (int)(TILE_SIZE * 0.9);
    private static final Color TANGIBLE_OUTLINE_COLOR = Color.BLACK; //the color of the outline around tiles that check collisions
    private static final Color INTANGIBLE_OUTLINE_COLOR = Color.WHITE; //the color of the outline around tiles that don't check collisions

    private final Color color;
    private final boolean blocking;

    public Tile(Color color, boolean blocking) {
        this.color = color;
        this.blocking = blocking;
    }

    /**
     * Draws a copy of this tile at the given location on the given graphics 
     * context.
     */
    public void drawAt(Graphics g, int x, int y) {
        if (blocking){
            g.setColor(TANGIBLE_OUTLINE_COLOR);
        } else {
            g.setColor(INTANGIBLE_OUTLINE_COLOR);
        }
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(color);
        g.fillRect(x + TILE_SPACING, y + TILE_SPACING, RECT_SIZE, RECT_SIZE);
    }

    @Override
    public void draw(Graphics g) {
        throw new UnsupportedOperationException("use drawAt instead");
    }

    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("color", color.getRGB())
            .add("blocking", blocking)
            .build();
    }

    public static Tile fromJson(JsonObject json) {
        return new Tile(
            new Color(json.getInt("color")),
            json.getBoolean("blocking")
        );
    }
}
