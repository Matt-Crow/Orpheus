
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
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import upgradables.AbstractUpgradable;
import util.SerialUtil;
import windows.Canvas;

/**
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
        
        System.out.println("In world canvas, world is " + world);
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
    
    private int[] retTranslate(){
		int[] ret = new int[2];
        //new upper left corner
		int x = (int) ((Master.getUser().getPlayer().getX() - getWidth() / 2) / getPriorZoom());
		int y = (int) ((Master.getUser().getPlayer().getY() - getHeight() / 2) / getPriorZoom());
		
		/*
		int minX = -(battlefield.getWidth() - getW());
		int minY = -(battlefield.getHeight() - getH());
		
		if(x < minX){
			x = minX;
		} else if (x > 0){
			x = 0;
		}
		
		if(y < minY){
			y = minY;
		} else if (y > 0){
			y = 0;
		}
		*/
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
    
    
    
    public World getWorld(){
        return world;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int[] trans = retTranslate();
        translate(-getTx() - trans[0], -getTy() - trans[1]);
        setZoom(0.5);
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
            SerialUtil.fromSerializedString(SerialUtil.serializeToString(t1));
        });
    }
}
