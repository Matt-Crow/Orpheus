package controls;

import entities.AbstractPlayer;
import entities.AbstractPlayerControlCommand;
import java.util.LinkedList;
import windows.Canvas;
import windows.EndOfFrameListener;

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
public class AbstractControlScheme<T extends AbstractPlayer> implements EndOfFrameListener{
    private final T targettedPlayer;
    private final LinkedList<AbstractPlayerControlCommand> receivedCommands;
    
    public AbstractControlScheme(T forPlayer){
        targettedPlayer = forPlayer;
        receivedCommands = new LinkedList<>();
    }
    
    public final T getPlayer(){
        return targettedPlayer;
    }
    
    public final void enqueueCommand(AbstractPlayerControlCommand cmd){
        receivedCommands.addLast(cmd);
    }

    @Override
    public void frameEnded(Canvas c) {
        while(!receivedCommands.isEmpty()){
            receivedCommands.poll().execute(targettedPlayer);
        }
    }
}
