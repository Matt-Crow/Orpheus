package orpheus.core.world.graph.playables;

import java.awt.Graphics;

import orpheus.core.world.graph.GraphElement;

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
}
