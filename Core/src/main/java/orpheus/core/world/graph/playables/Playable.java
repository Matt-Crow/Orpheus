package orpheus.core.world.graph.playables;

import java.awt.Graphics;

import javax.json.JsonObject;

import orpheus.core.world.graph.GraphElement;
import orpheus.core.world.graph.Orpheus;

/**
 * Playable is different from the other Graphables in that it does not support
 * drawing without specified coordinates.
 */
public interface Playable extends GraphElement {
    
    /**
     * Draws this object at the given coordinates on the given graphics context
     * @param g the graphics context to draw on
     * @param x the x coordinate to draw at on the graphics
     * @param y the y coordinate to draw at on the graphics
     */
    public void drawAt(Graphics g, int x, int y);

    public static Playable fromJson(JsonObject json) {
        var type = json.getString("type");
        switch (type) {
            case "AssembledBuild":
                return new AssembledBuild();
            case "Orpheus":
                return Orpheus.fromJson(json);
        }
        throw new IllegalArgumentException(String.format("Unsupported type: \"%s\"", type));
    }
}
