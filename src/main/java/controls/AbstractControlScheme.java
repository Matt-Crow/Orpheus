package controls;

import entities.AbstractPlayer;
import world.AbstractWorld;

/**
 * This acts as the base class
 * for AbstractPlayerControls,
 * 
 * @author Matt Crow
 */
public class AbstractControlScheme {
    private final AbstractWorld targettedWorld;
    private final String targettedEntityId;
    
    public AbstractControlScheme(AbstractWorld world, String playerId){
        targettedWorld = world;
        targettedEntityId = playerId;
    }
    
    public final AbstractWorld getWorld(){
        return targettedWorld;
    }
    public final String getPlayerId(){
        return targettedEntityId;
    }
    
    public final AbstractPlayer getPlayer(){
        return targettedWorld.getPlayerTeam().getMemberById(targettedEntityId);
    }
}
