package orpheus.client.gui.pages.play;

import java.util.function.Supplier;

import orpheus.core.world.graph.World;

/**
 * Supplies a World for a Canvas
 */
public interface WorldGraphSupplier extends Supplier<World> {
    
    public World get();
}
