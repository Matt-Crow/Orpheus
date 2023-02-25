package orpheus.client.gui.pages.play;

import java.awt.Graphics;

/**
 * Renders a World on a Canvas
 */
public interface WorldRenderer {
    
    /**
     * Renders a world on the given Graphics context.
     * @param g the Graphics context to render on.
     */
    public void draw(Graphics g);
}
