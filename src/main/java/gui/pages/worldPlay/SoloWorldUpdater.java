package gui.pages.worldPlay;

import world.TempWorld;

/**
 *
 * @author Matt Crow
 */
public class SoloWorldUpdater extends AbstractWorldUpdater {

    public SoloWorldUpdater(TempWorld world) {
        super(world, true);
    }

    @Override
    protected void updateWorld(TempWorld world) {
        world.updateParticles();
    }
}
