package orpheus.core.world.updaters;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import javax.swing.Timer;

import net.protocols.EndOfFrameListener;
import util.Settings;

/**
 * Updates either a world or its graph, but does not handle drawing, as that
 * should run on a seperate timer.
 * 
 * @author Matt Crow
 */
public class WorldUpdater {
    private final LinkedList<EndOfFrameListener> updateListeners;
    private final Timer updateTimer;
    private final boolean canPause;
    private boolean hasStarted;
    
    public WorldUpdater(boolean canPause){
        updateListeners = new LinkedList<>();
        updateTimer = new Timer(1000 / Settings.FPS, this::update);
        updateTimer.stop();
        this.canPause = canPause;
        hasStarted = false;
    }
    
    public void start(){
        if(hasStarted){
            throw new UnsupportedOperationException("Can only start WorldUpdater once.");
        }
        hasStarted = true;
        updateTimer.start();
    }
    
    public void pause(){
        if(canPause){
            updateTimer.stop();
        }
    }
    
    public void unPause(){
        if(canPause){
            updateTimer.start();
        }
    }
    
    public void addEndOfFrameListener(EndOfFrameListener eofl){
        updateListeners.add(eofl);
    }
    
    private void update(ActionEvent e){
        update();
    }
    
    private void update(){
        updateListeners.forEach((eofl)->eofl.frameEnded());
    }
}
