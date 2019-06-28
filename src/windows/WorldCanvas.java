
package windows;

import controllers.World;
import graphics.Tile;
import controllers.Master;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.Timer;
import battle.Team;
import customizables.Build;
import entities.PlayerControls;
import graphics.Map;
import graphics.MapLoader;
import gui.FileChooserUtil;
import java.awt.MouseInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import serialization.JsonUtil;
import upgradables.AbstractUpgradable;
import gui.Chat;

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
        
        Button b = new Button("Exit");
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
        registerKey(KeyEvent.VK_P, true, ()->togglePause());
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
            
            System.out.println("in world canvas paint component, world is: ");
            world.displayData();
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
        WorldCanvas c = new WorldCanvas();
        JFrame f = new JFrame();
        f.setContentPane(c);
        f.setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        AbstractUpgradable.loadAll();
        Build.loadAll();
        
        Master.getUser().getPlayer().applyBuild(Build.getBuildByName("Default Fire"));
        Team t1 = new Team("Test", Color.BLUE);
        t1.addMember(Master.getUser().getPlayer());
        t1.init(0, 50, 0);
        
        try {
            c.world.setMap(MapLoader.readCsv(WorldCanvas.class.getResourceAsStream("/testMap.csv")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Tile t = new Tile(0, 0, Color.BLACK);
        t.setBlocking(true);
        c.world.getMap()
            .addToTileSet(0, new Tile(0, 0, Color.WHITE))
            .addToTileSet(1, t);
        
        c.world.addTeam(t1);
        c.world.init();
        f.setVisible(true);
        f.revalidate();
        f.repaint();
        
        
        JsonObject obj = c.world.getMap().serializeJson();
        JsonUtil.pprint(obj, 0);
        c.world.setMap(Map.deserializeJson(obj));
        c.world.init();
        
        
        c.world.displayData();
        File saveTo = FileChooserUtil.chooseDir();
        if(saveTo != null){
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveTo.getAbsolutePath() + "/obj.ser"));
                out.writeObject(c.world);
                
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveTo.getAbsolutePath() + "/obj.ser"));
                World newWorld = (World)in.readObject();
                //Chat.log("Switching worlds in 3 seconds...");
                Timer time = new Timer(3000, (e)->{
                    newWorld.createCanvas();
                    f.setContentPane(newWorld.getCanvas());
                    //Chat.log("done!");
                    //Chat.log(Master.getUser().getPlayer().getX() + ", " + Master.getUser().getPlayer().getY());
                    Master.getUser().getPlayer().setWorld(newWorld);
                    //Chat.log(Master.getUser().getPlayer().getWorld() + " : " + newWorld);
                    c.world.displayData();
                    f.revalidate();
                    f.repaint();
                });
                time.setRepeats(false);
                time.start();
                
                //File file = new File(saveTo.getAbsolutePath() + "/map.csv");
                //MapLoader.saveCsv(new FileOutputStream(file), c.world.getMap());
            } catch (Exception ex) {
                Logger.getLogger(WorldCanvas.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }
    }
}
