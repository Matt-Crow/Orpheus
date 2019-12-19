package entities;

import ai.PlayerAI;
import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;

/**
 *
 * @author Matt
 */
public class AIPlayer extends AbstractPlayer{
    private PlayerAI playerAI;
    private final int level;
    private static final CharacterClass TEMP = CharacterClass.getCharacterClassByName("Default");
    
    public AIPlayer(String n, int lv) {
        super(n, lv);
        playerAI = null;
        level = lv;
    }
    
    public PlayerAI getPlayerAI(){
		return playerAI;
	}

    @Override
    public void playerInit() {
        playerAI = new PlayerAI(this);
        playerAI.setEnabled(true);
    }

    @Override
    public void playerUpdate() {
        playerAI.update();
    }

    @Override
    public double getStatValue(CharacterStatName n) {
        return TEMP.getStatValue(n);
    }
}
