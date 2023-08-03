package world.builds.characterClass;

import gui.graphics.CustomColors;

/**
 * provides stats for AI players
 */
public class DroneCharacterClass extends CharacterClass {
    
    private final int level;

    public DroneCharacterClass(int level) {
        super("Drone", CustomColors.BLACK, level, 3, 3, 3);
        this.level = level;
    }

    @Override
    public DroneCharacterClass copy() {
        return new DroneCharacterClass(level);
    }
}
