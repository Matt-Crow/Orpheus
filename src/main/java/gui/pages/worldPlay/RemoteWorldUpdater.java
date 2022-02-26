package gui.pages.worldPlay;

import world.TempWorld;

/**
 *
 * @author Matt Crow
 */
public class RemoteWorldUpdater extends AbstractWorldUpdater {

    public RemoteWorldUpdater(TempWorld world) {
        super(world, false);
    }

    @Override
    protected void updateWorld(TempWorld world) {
        world.updateParticles();
    }
}
