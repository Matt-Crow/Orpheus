
package windows;

import actives.LoadActives;
import controllers.World;
import graphics.ImageTile;
import graphics.Tile;
import controllers.Master;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.Timer;
import battle.Team;
import customizables.Build;
import customizables.LoadCharacterClasses;
import entities.PlayerControls;
import passives.LoadPassives;
import util.Chat;

/**
 * Test class for now
 * @author Matt Crow
 */
public class WorldCanvas extends DrawingPlane{
    private World world;
    private Timer timer;
    private boolean paused;
    private int i;
    
    public WorldCanvas(World w){
        super();
        world = w;
        
        Button b = new Button("Exit");
		b.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				switchTo(new MainCanvas());
			}
		});
		addMenuItem(b);
		
		Chat.addTo(this);
        resizeComponents(10, 10);
		resizeMenu(1);
        
        paused = true;
        timer = new Timer(1000 / Master.FPS, update());
        timer.setRepeats(true);
        timer.stop();
        i = 0;
        
        //change this later
        PlayerControls pc = new PlayerControls(Master.TRUEPLAYER);
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
            i++;
            repaint();
        };
    }
    
    private int[] retTranslate(){
		int[] ret = new int[2];
		int x = -Master.TRUEPLAYER.getX() + getW() / 2;
		int y = -Master.TRUEPLAYER.getY() + getH() / 2;
		
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
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
		setG(g);
		int[] trans = retTranslate();
		translate(trans[0], trans[1]);
		world.draw(getG());
		resetToInit();
		
		Master.TRUEPLAYER.drawHUD(getG());
        /*
        if(hostedBattle.shouldEnd()){
			drawMatchResolution(getG());
        }*/
        g.setColor(Color.red);
        g.fillRect(i, 0, 100, 100);
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
    /*
	public void drawMatchResolution(Graphics g){
		paused = true;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT);
		g.setColor(Color.yellow);
		g.drawString("The match is ended,", (int) (Master.CANVASWIDTH * 0.3), (int) (Master.CANVASHEIGHT * 0.3));
		g.drawString(hostedBattle.getWinner().getName(), (int) (Master.CANVASWIDTH * 0.5), (int) (Master.CANVASHEIGHT * 0.5));
		g.drawString("is victorious!", (int) (Master.CANVASWIDTH * 0.7), (int) (Master.CANVASHEIGHT * 0.7));
	}*/
    
    
    
    public static void main(String[] args){
        WorldCanvas c = new WorldCanvas();
        JFrame f = new JFrame();
        f.setContentPane(c);
        f.setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
        try {
            Image testImage = ImageIO.read(c.getClass().getResourceAsStream("/testImage.PNG"));
            c.world.setBlock(0, new ImageTile(0, 0, testImage));
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
        LoadActives.load();
        LoadPassives.load();
		LoadCharacterClasses.load();
        
        Tile t = new Tile(0, 0, Color.BLACK);
        t.setBlocking(true);
        
        Master.TRUEPLAYER.applyBuild(Build.getBuildByName("Default Fire"));
        Team t1 = new Team("Test", Color.BLUE);
        t1.addMember(Master.TRUEPLAYER);
        t1.init(0, 50, 0);
        
        c.world
            .setBlock(0, new Tile(0, 0, Color.WHITE))
            .setBlock(1, t)
            .setTile(0, 0, 1)
            .setTile(0, 1, 1)
            .setTile(1, 1, 1)
            .setTile(2, 1, 1)
            .setTile(2, 2, 1)
            .initTiles();
        c.world.addTeam(t1);
        f.setVisible(true);
        f.revalidate();
        f.repaint();
        
    }
}
