package gui.pages.worldPlay;

import java.awt.event.ActionEvent;
import javax.swing.Timer;
import util.Settings;
import world.AbstractWorldShell;

/**
 * this should handle updating, while WorldCanvas handles rendering
 * 
 * @author Matt Crow
 */
public class WorldUpdater {
    private final AbstractWorldShell world;
    private final Timer updateTimer;
    private final boolean canPause;
    private boolean hasStarted;
    
    public WorldUpdater(AbstractWorldShell world, boolean canPause){
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
    
    private void update(ActionEvent e){
        update();
    }
    
    private void update(){
        world.update();
    }
}
