package orpheus.client.gui.pages.play;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Supplies a World graph for a Canvas.
 * When running locally, we have access to the original world,
 * but when running remotely, we only have access to the graph.
 * 
 * This is like a discriminated union of a world and a world graph.
 */
public final class WorldGraphSupplier implements Supplier<orpheus.core.world.graph.World> {
    private final Optional<world.World> world;
    private volatile Optional<orpheus.core.world.graph.World> graph;

    private WorldGraphSupplier(Optional<world.World> world, Optional<orpheus.core.world.graph.World> graph) {
        this.world = world;
        this.graph = graph;
    }

    public static WorldGraphSupplier fromWorld(world.World world) {
        return new WorldGraphSupplier(Optional.of(world), Optional.empty());
    }

    public static WorldGraphSupplier fromGraph(orpheus.core.world.graph.World graph) {
        return new WorldGraphSupplier(Optional.empty(), Optional.of(graph));
    }

    public void setWorld(orpheus.core.world.graph.World graph) {
        this.graph = Optional.of(graph);
    }

    public orpheus.core.world.graph.World get() {
        // there is probably a more elegant way of doing this
        if (graph.isPresent()) {
            return graph.get();
        }
        return world.map(w -> w.toGraph()).orElseThrow();
    }
}
