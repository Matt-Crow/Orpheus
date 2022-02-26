package gui.pages.worldPlay;

import world.AbstractWorldShell;

/**
 *
 * @author Matt Crow
 */
public class SoloWorldUpdater extends AbstractWorldUpdater {

    public SoloWorldUpdater(AbstractWorldShell world) {
        super(world, true);
    }

    @Override
    protected void updateWorld(AbstractWorldShell world) {
        world.updateMinigame(); // or world.update()?
        world.updateParticles();
    }
}
