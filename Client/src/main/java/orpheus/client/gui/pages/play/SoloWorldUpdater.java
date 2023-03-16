package orpheus.client.gui.pages.play;

import orpheus.core.world.graph.particles.Particles;
import orpheus.core.world.updaters.AbstractWorldUpdater;
import world.World;

/**
 *
 * @author Matt Crow
 */
public class SoloWorldUpdater extends AbstractWorldUpdater {
    private final WorldGraphSupplier graph;
    private final Particles particles;
    private final World world;

    public SoloWorldUpdater(WorldGraphSupplier graph, Particles particles, World world) {
        super(true);
        this.graph = graph;
        this.particles = particles;
        this.world = world;
    }

    @Override
    protected void doUpdate() {
        world.update();
        graph.get().spawnParticlesInto(particles);
        particles.update();
    }
}
