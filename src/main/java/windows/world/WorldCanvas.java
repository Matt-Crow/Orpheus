
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
import entities.PlayerControls;
import java.awt.Graphics2D;
import java.io.IOException;
import entities.HumanPlayer;
import util.SerialUtil;
import windows.Canvas;
import world.HostWorld;
import world.RemoteProxyWorld;
import world.SoloWorld;

/**
 * P: pause
 * Z: zoom in
 * X: zoom out
 * 
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas{
    private AbstractWorld world;
    private Timer timer;
    private boolean paused;
    
    public WorldCanvas(AbstractWorld w){
        super();
        world = w;
        
        w.setCanvas(this);
        
        paused = true;
        timer = new Timer(1000 / Master.FPS, (ActionEvent e) -> {
            world.update();
            endOfFrame();
            repaint();
        });
        timer.setRepeats(true);
        timer.stop();
        
        PlayerControls pc = new PlayerControls(Master.getUser().getPlayer(), world instanceof RemoteProxyWorld);
		addMouseListener(pc);
        addEndOfFrameListener(pc);
        pc.registerControlsTo(this);
        if(world instanceof RemoteProxyWorld || world instanceof HostWorld){
            //resume, but cannot pause
            togglePause();
        }else{
            registerKey(KeyEvent.VK_P, true, ()->togglePause());
        }
        registerKey(KeyEvent.VK_Z, true, ()->zoomIn());
        registerKey(KeyEvent.VK_X, true, ()->zoomOut());
        setZoom(0.5);
    }
    public WorldCanvas(int i){
        this(new SoloWorld(i)); //temporary
    }
    public WorldCanvas(){
        this(100);
    }
    
    private void togglePause(){
        paused = !paused;
        if(paused){
            timer.stop();
        } else {
            timer.start();
        }
        repaint();
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
        Master.getUser().initPlayer().getPlayer().applyBuild(Master.getDataSet().getDefaultBuild());
        AbstractWorld w = AbstractWorld.createDefaultBattle();
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam("Rando", Color.yellow, 1, 1);
        WorldCanvas c = new WorldCanvas(w);
        
        MainWindow mw = MainWindow.getInstance();
        WorldPage wp = new WorldPage();
        wp.setCanvas(c);
        mw.switchToPage(wp);
        
        Battle b = new Battle(10, 5);
        
        t1.addMember(Master.getUser().getPlayer());
        w.setPlayerTeam(t1).setEnemyTeam(t2);
        b.setHost(w);
        w.setCurrentMinigame(b);
        w.init();
        
        Master.getUser().setRemotePlayerId(Master.getUser().getPlayer().id);
        
        w.getCanvas().registerKey(KeyEvent.VK_T, true, ()->{
            Master.getUser().getPlayer().getActionRegister().displayData();
            Master.getUser().getPlayer().listStatuses();
        });
        
        
        //now to try serializing it...
        String serial = w.serializeToString();
        AbstractWorld newWorld = AbstractWorld.fromSerializedString(serial);
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.setPlayer((HumanPlayer)newWorld.getPlayerTeam().getMemberById(me.getRemotePlayerId()));
        newWorld.createCanvas();
        newWorld.setCurrentMinigame(new Battle(10, 5));
        newWorld.init();
        
        wp = new WorldPage();
        wp.setCanvas(newWorld.getCanvas());
        mw.switchToPage(wp);
        
        
        newWorld.getCanvas().registerKey(KeyEvent.VK_S, true, ()->{
            Team t = newWorld.getPlayerTeam();
            System.out.println("Total entities to serialize: " + t.length());
            String s = SerialUtil.serializeToString(t);
            Team tClone = (Team)SerialUtil.fromSerializedString(s);
            System.out.println("Total entities deserialized: " + tClone.length());
        });
    }
}
