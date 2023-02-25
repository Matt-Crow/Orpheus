package orpheus.client.gui.pages.play;

import java.awt.Graphics;

import orpheus.core.world.graph.World;

/**
 * Renders a world that exists on a remote server
 */
public class RemoteWorldRenderer implements WorldRenderer {
    private volatile World world;

    public RemoteWorldRenderer(World world) {
        this.world = world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void draw(Graphics g) {
        world.draw(g);
    }
}
