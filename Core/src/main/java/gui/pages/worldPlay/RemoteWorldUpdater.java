package gui.pages.worldPlay;

import world.World;

/**
 *
 * @author Matt Crow
 */
public class RemoteWorldUpdater extends AbstractWorldUpdater {

    public RemoteWorldUpdater(World world) {
        super(world, false);
    }

    @Override
    protected void updateWorld(World world) {
        world.updateParticles();
    }
}
