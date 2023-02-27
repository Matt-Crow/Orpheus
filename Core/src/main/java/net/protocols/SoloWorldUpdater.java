package net.protocols;

import world.World;

/**
 *
 * @author Matt Crow
 */
public class SoloWorldUpdater extends AbstractWorldUpdater {

    public SoloWorldUpdater(World world) {
        super(world, true);
    }

    @Override
    protected void updateWorld(World world) {
        world.update();
    }
}
