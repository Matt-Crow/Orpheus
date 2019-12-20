package entities;

import ai.PlayerAI;
import customizables.characterClass.CharacterStatName;

/**
 *
 * @author Matt
 */
public class AIPlayer extends AbstractPlayer{
    private final PlayerAI playerAI;
    private final int level;
     
    public AIPlayer(String n, int lv) {
        super(n, lv);
        playerAI = new PlayerAI(this);
        level = lv;
    }
    
    public PlayerAI getPlayerAI(){
		return playerAI;
	}

    @Override
    public void playerInit() {
        playerAI.init();
    }

    @Override
    public void playerUpdate() {
        playerAI.update();
    }

    @Override
    public double getStatValue(CharacterStatName n) {
        return 0.1 * level * n.getDefaultValue();
    }
}
