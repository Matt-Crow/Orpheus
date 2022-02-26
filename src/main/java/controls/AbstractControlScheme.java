package controls;

import world.entities.AbstractPlayer;
import world.TempWorld;

/**
 * This acts as the base class
 * for PlayerControls.
 * 
 * In the future, this may also be used in AI
 * 
 * @author Matt Crow
 */
public class AbstractControlScheme {
    private final TempWorld targettedWorld;
    private final String targettedEntityId; // can't store as AbstractPlayer b/c of serialization
    
    public AbstractControlScheme(TempWorld world, String playerId){
        targettedWorld = world;
        targettedEntityId = playerId;
    }
    
    public final TempWorld getWorld(){
        return targettedWorld;
    }
    public final String getPlayerId(){
        return targettedEntityId;
    }
    
    public final AbstractPlayer getPlayer(){
        return targettedWorld.getPlayers().getMemberById(targettedEntityId);
    }
}
