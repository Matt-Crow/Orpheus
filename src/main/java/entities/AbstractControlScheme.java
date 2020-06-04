package entities;

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
 */
public class AbstractControlScheme implements EndOfFrameListener{
    private final AbstractPlayer targettedPlayer;
    private final LinkedList<AbstractPlayerControlCommand> receivedCommands;
    
    public AbstractControlScheme(AbstractPlayer forPlayer){
        targettedPlayer = forPlayer;
        receivedCommands = new LinkedList<>();
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
