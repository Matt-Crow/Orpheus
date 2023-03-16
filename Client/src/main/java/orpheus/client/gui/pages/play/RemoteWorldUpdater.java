package orpheus.client.gui.pages.play;

import orpheus.core.world.graph.particles.Particles;
import orpheus.core.world.updaters.AbstractWorldUpdater;

/**
 *
 * @author Matt Crow
 */
public class RemoteWorldUpdater extends AbstractWorldUpdater {
    private final WorldGraphSupplier world;
    private final Particles particles;

    public RemoteWorldUpdater(WorldGraphSupplier world, Particles particles) {
        super(false);
        this.world = world;
        this.particles = particles;
    }

    @Override
    protected void doUpdate() {
        world.get().spawnParticlesInto(particles);
        particles.update();
    }
}
