package controls;

import entities.AbstractPlayer;
import entities.AbstractPlayerControlCommand;
import java.util.LinkedList;
import windows.Canvas;
import windows.EndOfFrameListener;
import world.AbstractWorld;

/**
 * In the future, this will act as the base class
 * for AI and PlayerControls,
 * hopefully this will also allow me to remove the
 * "is remote" boolean from PlayerControls as well.
 * Maybe make this extend into a RemoteControl class
 * that receives server messages instead of key input?
 * @author Matt Crow
 * @param <T> the subclass of AbstractPlayer this is controlling
 */
public class AbstractControlScheme<T extends AbstractPlayer> {
    private final T targettedPlayer;
    private final String targettedEntityId;
    
    public AbstractControlScheme(T forPlayer){
        targettedPlayer = forPlayer;
        targettedEntityId = forPlayer.id;
    }
    
    public final T getPlayer(){
        return targettedPlayer;
    }
    
    // I need to switch to this method so serialization works
    public final T getPlayerInWorld(AbstractWorld w){
        return (T)w.getPlayerTeam().getMemberById(targettedEntityId);
    }
}
