package entities;

import ai.PlayerAI;

/**
 *
 * @author Matt
 */
public class AIPlayer extends AbstractPlayer{
    private PlayerAI playerAI;
    
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
}
