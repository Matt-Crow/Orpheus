package orpheus.client.gui.pages.play;

import java.awt.Graphics;

import world.World;

/**
 * Renders a world that exists on the client
 */
public class LocalWorldRenderer implements WorldRenderer {

    private final World world;

    public LocalWorldRenderer(World world) {
        this.world = world;
    }

    @Override
    public void draw(Graphics g) {
        world.toGraph().draw(g);
    }
}
