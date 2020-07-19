package controls;

import entities.AbstractPlayer;
import world.AbstractWorld;

/**
 * This acts as the base class
 * for AbstractPlayerControls,
 * 
 * @author Matt Crow
 * @param <T> the subclass of AbstractPlayer this is controlling
 */
public class AbstractControlScheme<T extends AbstractPlayer> {
    private final AbstractWorld targettedWorld;
    private final String targettedEntityId;
    
    public AbstractControlScheme(T forPlayer, AbstractWorld world){
        targettedWorld = world;
        targettedEntityId = forPlayer.id;
    }
    
    public final T getPlayer(){
        return (T)targettedWorld.getPlayerTeam().getMemberById(targettedEntityId);
    }
}
