package orpheus.client.gui.pages.play;

import orpheus.core.world.graph.World;

/**
 * Supplies a world that exists on a remote server
 */
public class RemoteWorldSupplier implements WorldGraphSupplier {
    private volatile World world;

    public RemoteWorldSupplier(World world) {
        this.world = world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public World get() {
        return world;
    }
}
