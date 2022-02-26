package gui.pages.worldPlay;

import gui.pages.EndOfFrameListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import javax.swing.Timer;
import util.Settings;
import world.TempWorld;

/**
 * this should handle updating, while WorldCanvas handles rendering
 * 
 * @author Matt Crow
 */
public abstract class AbstractWorldUpdater {
    private final LinkedList<EndOfFrameListener> updateListeners;
    private final TempWorld world;
    private final Timer updateTimer;
    private final boolean canPause;
    private boolean hasStarted;
    
    public AbstractWorldUpdater(TempWorld world, boolean canPause){
        updateListeners = new LinkedList<>();
        this.world = world;
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
        updateWorld(world);
        updateListeners.forEach((eofl)->eofl.frameEnded());
    }
    
    protected abstract void updateWorld(TempWorld world);
}
