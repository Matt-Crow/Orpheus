
package windows.world;

import battle.Battle;
import world.AbstractWorld;
import controllers.Master;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import battle.Team;
import controllers.MainWindow;
import controllers.User;
import controls.AbstractPlayerControls;
import controls.SoloPlayerControls;
import java.awt.Graphics2D;
import java.io.IOException;
import entities.HumanPlayer;
import util.SerialUtil;
import windows.Canvas;
import world.HostWorld;
import world.WorldContent;

/**
 * P: pause
 * Z: zoom in
 * X: zoom out
 * 
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas{
    private AbstractWorld world;
    private final Timer timer;
    private boolean paused;
    private boolean pauseEnabled;
    
    public WorldCanvas(AbstractWorld w){
        super();
        world = w;
        
        w.setCanvas(this);
        
        paused = true;
        pauseEnabled = true;
        timer = new Timer(1000 / Master.FPS, (ActionEvent e) -> {
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
    }
    
    /**
     * Remember to use this if you want to control a player!
     * @param pc 
     */
    public final void addPlayerControls(AbstractPlayerControls pc){
        addMouseListener(pc);
        addEndOfFrameListener(pc);
        pc.registerControlsTo(this);
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
    
    public AbstractWorld getWorld(){
        return world;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        centerOn(
            Master.getUser().getPlayer().getX(), 
            Master.getUser().getPlayer().getY()
        );
		Graphics2D g2d = applyTransforms(g);
		
		world.draw(g2d);
        
		reset();
        
		Master.getUser().getPlayer().drawHUD(g2d, this);
        
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
    
    
    
    public static void main(String[] args) throws IOException{
        User user = Master.getUser();
        HumanPlayer player = user.initPlayer().getPlayer();
        
        player.applyBuild(Master.getDataSet().getDefaultBuild());
        
        HostWorld world = new HostWorld(WorldContent.createDefaultBattle());
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam("Rando", Color.yellow, 1, 1);
        
        WorldCanvas canvas = new WorldCanvas(world);
        canvas.addPlayerControls(new SoloPlayerControls(player, world));
        MainWindow mw = MainWindow.getInstance();
        WorldPage wp = new WorldPage();
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        
        
        Battle b = new Battle(10, 5);
        
        t1.addMember(player);
        world.setPlayerTeam(t1).setEnemyTeam(t2);
        b.setHost(world);
        world.setCurrentMinigame(b);
        world.init();
        
        user.setRemotePlayerId(player.id);
        
        
        //now to try serializing it...
        String serial = world.getContent().serializeToString();
        
        WorldContent newContent = WorldContent.fromSerializedString(serial);
        world.setContent(newContent);
        
        //user.setPlayer((HumanPlayer)newContent.getPlayerTeam().getMemberById(user.getRemotePlayerId()));
        
        //newWorld.init();
        
        wp = new WorldPage();
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        canvas.setPauseEnabled(true);
        world.getCanvas().registerKey(KeyEvent.VK_S, true, ()->{
            Team t = world.getPlayerTeam();
            System.out.println("Total entities to serialize: " + t.length());
            String s = SerialUtil.serializeToString(t);
            Team tClone = (Team)SerialUtil.fromSerializedString(s);
            System.out.println("Total entities deserialized: " + tClone.length());
        });
    }
}
