
package windows.world;

import battle.Battle;
import controllers.World;
import controllers.Master;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import battle.Team;
import controllers.User;
import customizables.Build;
import entities.PlayerControls;
import entities.TruePlayer;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import upgradables.AbstractUpgradable;
import util.SerialUtil;
import windows.Canvas;

/**
 * P: pause
 * Z: zoom in
 * X: zoom out
 * 
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas{
    private World world;
    private Timer timer;
    private boolean paused;
    
    public WorldCanvas(World w){
        super();
        world = w;
        
        w.setCanvas(this);
        
        paused = true;
        timer = new Timer(1000 / Master.FPS, update());
        timer.setRepeats(true);
        timer.stop();
        
        PlayerControls pc = new PlayerControls(Master.getUser().getPlayer(), world.isRemotelyHosted());
		addMouseListener(pc);
        pc.registerControlsTo(this);
        if(world.isRemotelyHosted() || world.isHosting()){
            //resume, but cannot pause
            togglePause();
        }else{
            registerKey(KeyEvent.VK_P, true, ()->togglePause());
        }
        registerKey(KeyEvent.VK_Z, true, ()->zoomIn());
        registerKey(KeyEvent.VK_X, true, ()->zoomOut());
        setZoom(0.75);
    }
    public WorldCanvas(int i){
        this(new World(i));
    }
    public WorldCanvas(){
        this(new World(100));
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
    private ActionListener update(){
        return (ActionEvent e) -> {
            world.update();
            repaint();
        };
    }
    
    public World getWorld(){
        return world;
    }
    
    /**
     * Centers the "camera" on a given point.
     * @param x
     * @param y 
     */
    private void centerOn(int x, int y){
        translate(
            -(int)(x * getZoom() - getWidth() / 2),
            -(int)(y * getZoom() -  getHeight() / 2)
        );
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
        
        if(world.getCurrentMinigame() != null && world.getCurrentMinigame().shouldEnd()){
			drawMatchResolution(g2d);
        }
        if(paused){
            drawPause(g2d);
        }
    }
    public void drawPause(Graphics g){
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT);
		g.setColor(Color.red);
		g.drawString("The game is paused", (int) (Master.CANVASWIDTH * 0.3), (int) (Master.CANVASHEIGHT * 0.3));
		g.drawString("Press 'p' to continue", (int) (Master.CANVASWIDTH * 0.4), (int) (Master.CANVASHEIGHT * 0.5));
	}
    
	public void drawMatchResolution(Graphics g){
		paused = true;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT);
		g.setColor(Color.yellow);
		g.drawString("The match is ended,", (int) (Master.CANVASWIDTH * 0.3), (int) (Master.CANVASHEIGHT * 0.3));
		g.drawString(world.getCurrentMinigame().getWinner().getName(), (int) (Master.CANVASWIDTH * 0.5), (int) (Master.CANVASHEIGHT * 0.5));
		g.drawString("is victorious!", (int) (Master.CANVASWIDTH * 0.7), (int) (Master.CANVASHEIGHT * 0.7));
	}
    
    
    
    public static void main(String[] args) throws IOException{
        AbstractUpgradable.loadAll();
        Build.loadAll();
        Master.getUser().initPlayer().getPlayer().applyBuild(Build.getBuildByName("Default Fire"));
        World w = World.createDefaultBattle();
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam("Rando", Color.yellow, 1);
        WorldCanvas c = new WorldCanvas(w);
        JFrame f = new JFrame();
        f.setContentPane(c);
        f.setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Battle b = new Battle();
        
        t1.addMember(Master.getUser().getPlayer());
        w.addTeam(t1).addTeam(t2);
        b.setHost(w);
        w.setCurrentMinigame(b);
        w.init();
        
        Master.getUser().setRemoteTeamId(t1.getId()).setRemotePlayerId(Master.getUser().getPlayer().id);
        
        //now to try serializing it...
        String serial = w.serializeToString();
        World newWorld = World.fromSerializedString(serial);
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.setPlayer((TruePlayer)newWorld.getTeamById(me.getRemoteTeamId()).getMemberById(me.getRemotePlayerId()));
        newWorld.createCanvas();
        newWorld.setCurrentMinigame(new Battle());
        newWorld.init();
        
        f.setContentPane(newWorld.getCanvas());
        
        f.setVisible(true);
        f.revalidate();
        f.repaint();
        
        newWorld.getCanvas().registerKey(KeyEvent.VK_S, true, ()->{
            File file = new File("C:/Users/Matt/Desktop/obj.ser");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(SerialUtil.serializeToString(newWorld.getTeams()));
                oos.close();
                System.out.println("done serial");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WorldCanvas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WorldCanvas.class.getName()).log(Level.SEVERE, null, ex);
            } catch (StackOverflowError ex){
                ex.printStackTrace();
            }
            //SerialUtil.fromSerializedString(SerialUtil.serializeToString(t1));
        });
    }
}
