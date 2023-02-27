package orpheus.client.gui.pages.play;

import world.World;

/**
 * Supplies a world that exists on the client
 */
public class LocalWorldSupplier implements WorldGraphSupplier {

    private final World world;

    public LocalWorldSupplier(World world) {
        this.world = world;
    }

    @Override
    public orpheus.core.world.graph.World get() {
        return world.toGraph();
    }
}
