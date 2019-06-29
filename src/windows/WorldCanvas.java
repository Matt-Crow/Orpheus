
package windows;

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
import java.awt.MouseInfo;
import java.io.IOException;
import upgradables.AbstractUpgradable;
import gui.Chat;
import javax.swing.JButton;

/**
 * Test class for now
 * @author Matt Crow
 */
public class WorldCanvas extends DrawingPlane{
    private World world;
    private Timer timer;
    private boolean paused;
    private Chat chat;
    
    public WorldCanvas(World w){
        super();
        world = w;
        
        w.setCanvas(this);
        
        JButton b = new JButton("Exit");
		b.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
                switchTo(new MainCanvas());
			}
		});
		addMenuItem(b);
		
        chat = new Chat();
		add(chat);
        resizeComponents(10, 10);
		resizeMenu(1);
        
        paused = true;
        timer = new Timer(1000 / Master.FPS, update());
        timer.setRepeats(true);
        timer.stop();
        
        //change this later
        PlayerControls pc = new PlayerControls(Master.getUser().getPlayer());
		addMouseListener(pc);
        pc.registerControlsTo(this);
        if(world.isRemotelyHosted() || world.isHosting()){
            //resume, but cannot pause
            togglePause();
        }else{
            registerKey(KeyEvent.VK_P, true, ()->togglePause());
        }
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
    }
    private ActionListener update(){
        return (ActionEvent e) -> {
            world.update();
            repaint();
        };
    }
    
    private int[] retTranslate(){
		int[] ret = new int[2];
		int x = -Master.getUser().getPlayer().getX() + getW() / 2;
		int y = -Master.getUser().getPlayer().getY() + getH() / 2;
		
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
    
    /**
     * Returns the point on this canvas where the mouse cursor is located,
     * this does account for translations
     * @return the x coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseX(){
        int mouseX = (int)MouseInfo.getPointerInfo().getLocation().getX();
        int[] translation = getLastTransform();
        return mouseX - translation[0];
    }
    
    /**
     * Returns the point on this canvas where the mouse cursor is located,
     * this does account for translations
     * @return the y coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseY(){
        int mouseY = (int)MouseInfo.getPointerInfo().getLocation().getY();
        int[] translation = getLastTransform();
        return mouseY - translation[1];
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
		setG(g);
		int[] trans = retTranslate();
		translate(trans[0], trans[1]);
		world.draw(getG());
        
		resetToInit();
		
		Master.getUser().getPlayer().drawHUD(getG());
        
        if(world.getCurrentMinigame() != null && world.getCurrentMinigame().shouldEnd()){
			drawMatchResolution(getG());
        }
        if(paused){
            drawPause(getG());
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
        
        Battle b = new Battle(
            c,
            t1,
            t2
        );
        
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
        newWorld.setCurrentMinigame(new Battle(
            newWorld.getCanvas(),
            newWorld.getTeams()[0],
            newWorld.getTeams()[1]
        ));
        newWorld.init();
        
        f.setContentPane(newWorld.getCanvas());
        
        f.setVisible(true);
        f.revalidate();
        f.repaint();
    }
}
