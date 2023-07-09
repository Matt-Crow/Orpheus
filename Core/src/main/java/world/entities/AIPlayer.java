package world.entities;

import java.util.UUID;

import controls.ai.PlayerAI;
import world.World;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterStatName;

/**
 * TODO merge AbstractPlayer, HumanPlayer, and AIPlayer into a new Player class
 * @author Matt
 */
public class AIPlayer extends AbstractPlayer {

    private final PlayerAI playerAI;
    private final int level;

    public AIPlayer(World inWorld, String n, int lv) {
        super(inWorld, n, lv, UUID.randomUUID(), MeleeActive.makeBasicAttack());
        playerAI = new PlayerAI(this); // TODO extract this so it's a layer above this
        level = lv;
    }

    @Override
    public void init() {
        super.init();
        playerAI.init();
    }

    @Override
    public void update() {
        super.update();
        playerAI.update();
    }

    @Override
    public double getStatValue(CharacterStatName n) {
        return 0.1 * level * n.getDefaultValue();
    }
}
