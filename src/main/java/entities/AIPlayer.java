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
    
    private static final CharacterClass TEMP = CharacterClass.getCharacterClassByName("Default");
    
    public AIPlayer(String n) {
        super(n);
        playerAI = null;
        
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
