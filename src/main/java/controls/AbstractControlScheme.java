package controls;

import entities.AbstractPlayer;
import world.AbstractWorldShell;

/**
 * This acts as the base class
 * for AbstractPlayerControls.
 * 
 * In the future, this may also be used in AI
 * 
 * @author Matt Crow
 */
public class AbstractControlScheme {
    private final AbstractWorldShell targettedWorld;
    private final String targettedEntityId;
    
    public AbstractControlScheme(AbstractWorldShell world, String playerId){
        targettedWorld = world;
        targettedEntityId = playerId;
    }
    
    public final AbstractWorldShell getWorld(){
        return targettedWorld;
    }
    public final String getPlayerId(){
        return targettedEntityId;
    }
    
    public final AbstractPlayer getPlayer(){
        return targettedWorld.getPlayerTeam().getMemberById(targettedEntityId);
    }
}
