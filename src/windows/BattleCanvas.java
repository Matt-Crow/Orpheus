package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import battle.Battlefield;
import initializers.Run;

public class BattleCanvas extends JPanel implements KeyListener{
	public static final long serialVersionUID = 1L;
	Battlefield b;
	private int w;
	private int h;
	private int s;
	private Timer timer;
	private int FPS;
	private String lastDirX;
	private String lastDirY;
	
	public BattleCanvas(int width, int height, int tileSize){
		w = width;
		h = height;
		s = tileSize;
		setLayout(null);
		setBackground(Color.black);
		b = new Battlefield(w, h, s);
		Run.player.setCoords(b.getCenter()[0] + 200, b.getCenter()[1]);
		setFocusable(true);
		addKeyListener(this);
		FPS = 20;
		lastDirX = " ";
		lastDirY = " ";
	}
	
	public int[] retTranslate(){
		int[] ret = new int[2];
		int x = Run.player.getX() - b.getCenter()[0];
		int y = Run.player.getY() - b.getCenter()[1];
		
		if(x < 0){
			x = 0;
		} else if (x > w * s){
			x = w * s;
		}
		if(y < 0){
			y = 0;
		} else if (y > h * s){
			y = h * s;
		}
		return ret;
	}
	
	public void keyPressed(KeyEvent k){
		//out.print("Pressed: ");
		//out.println(k.getKeyCode());
		switch(k.getKeyCode()){
			case 38:
				lastDirY = "N";
				Run.player.setMoving(true);
				break;
			case 37:
				lastDirX = "W";
				Run.player.setMoving(true);
				break;
			case 40:
				lastDirY = "S";
				Run.player.setMoving(true);
				break;
			case 39:
				lastDirX = "E";
				Run.player.setMoving(true);
				break;
		}
	}
	public void keyTyped(KeyEvent k){
		
	}
	public void keyReleased(KeyEvent k){
		Run.player.setMoving(false);
	}
	public String lastDir(){
		String dir = "";
		if(lastDirY != " "){
			dir = lastDirY;
		}
		if(lastDirX != " "){
			dir += lastDirX;
		}
		return dir;
	}
	ActionListener update = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	          Run.player.setDirection(lastDir());
	          Run.player.move();
	          repaint();
	      }
	  };
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		b.draw(g);
		g.setColor(Color.red);
		g.fillRect(Run.player.getX(), Run.player.getY(), 10, 10);
		timer = new Timer(1000 / FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
}
