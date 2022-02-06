package gui.pages.worldPlay;

import world.AbstractWorldShell;
import util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import controls.PlayerControls;
import world.entities.AbstractPlayer;
import java.awt.Graphics2D;
import gui.pages.Canvas;

/**
 * P: pause
 * Z: zoom in
 * X: zoom out
 * 
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas{
    private AbstractWorldShell world;
    private final Timer timer;
    private boolean paused;
    private boolean pauseEnabled;
    
    private String focusedEntityId;
    private HeadsUpDisplay hud;
    
    public WorldCanvas(AbstractWorldShell w){
        super();
        world = w;
        
        w.setCanvas(this);
        
        paused = true;
        pauseEnabled = true;
        timer = new Timer(1000 / Settings.FPS, (ActionEvent e) -> {
            world.update();
            endOfFrame();
            repaint();
        });
        timer.setRepeats(true);
        timer.stop();
        
        registerKey(KeyEvent.VK_Z, true, ()->zoomIn());
        registerKey(KeyEvent.VK_X, true, ()->zoomOut());
        registerKey(KeyEvent.VK_P, true, ()->togglePause());
        setZoom(0.5);
        
        focusedEntityId = null;
    }
    
    /**
     * Remember to use this if you want to control a player!
     * @param pc 
     */
    public final void addPlayerControls(PlayerControls pc){
        addMouseListener(pc);
        addEndOfFrameListener(pc);
        pc.registerControlsTo(this);
        focusedEntityId = pc.getPlayer().id;
        hud = new HeadsUpDisplay(this, pc.getPlayer());
    }
    
    public void setPauseEnabled(boolean canPause){
        pauseEnabled = canPause;
        if(!canPause && !timer.isRunning()){
            paused = false;
            timer.start();
        }
    }
    private void togglePause(){
        if(pauseEnabled){
            paused = !paused;
            if(paused){
                timer.stop();
            } else {
                timer.start();
            }
            repaint();
        }
    }
    
    //need this for when leaving world page
    public void stop(){
        paused = true;
        timer.stop();
    }
    
    public AbstractWorldShell getWorldShell(){
        return world;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(focusedEntityId == null){
            centerOn(0, 0);
        } else {
            AbstractPlayer focus = world.getPlayerTeam().getMemberById(focusedEntityId);
            centerOn(
                focus.getX(),
                focus.getY()
            );
        }
		Graphics2D g2d = applyTransforms(g);
		
		world.draw(g2d);
        
		reset();
        
		if(focusedEntityId != null){
            hud.draw(g);
        }
        
        if(world.getCurrentMinigame() != null && world.getCurrentMinigame().isDone()){
			drawMatchResolution(g2d);
        }
        if(paused){
            drawPause(g2d);
        }
    }
    public void drawPause(Graphics g){
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);
		g.drawString("The game is paused", (int) (getWidth() * 0.3), (int) (getHeight() * 0.3));
		g.drawString("Press 'p' to continue", (int) (getWidth() * 0.4), (int) (getHeight() * 0.5));
	}
    
	public void drawMatchResolution(Graphics g){
		paused = true;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.yellow);
		g.drawString("The match is ended,", (int) (getWidth() * 0.3), (int) (getHeight() * 0.3));
		g.drawString(world.getCurrentMinigame().getWinner().getName(), (int) (getWidth() * 0.5), (int) (getHeight() * 0.5));
		g.drawString("is victorious!", (int) (getWidth() * 0.7), (int) (getHeight() * 0.7));
	}
}
