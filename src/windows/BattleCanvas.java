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
import resources.Op;

public class BattleCanvas extends JPanel implements KeyListener{
	public static final long serialVersionUID = 1L;
	Battlefield b;
	private int w;
	private int h;
	private int s;
	private Timer timer;
	private int FPS;
	private int turnCooldown;
	
	public BattleCanvas(int width, int height, int tileSize){
		w = width;
		h = height;
		s = tileSize;
		setLayout(null);
		setBackground(Color.black);
		b = new Battlefield(w, h, s);
		Run.player.setCoords(b.getCenter()[0], b.getCenter()[1]);
		setFocusable(true);
		addKeyListener(this);
		FPS = 20;
		turnCooldown = 0;
	}
	
	public int[] retTranslate(){
		int[] ret = new int[2];
		int x = -Run.player.getX() + 500;
		int y = -Run.player.getY() + 500;
		
		if(x < -w * s + 1000){
			x = -w * s + 1000;
		} else if (x > 0){
			x = 0;
		}
		
		if(y < -h * s + 1000){
			y = -h * s + 1000;
		} else if (y > 0){
			y = 0;
		}
		
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	public void keyPressed(KeyEvent k){
		//out.print("Pressed: ");
		//out.println(k.getKeyCode());
		switch(k.getKeyCode()){
			case 38:
				Run.player.setMoving(true);
				break;
			case 37:
				if(turnCooldown <= 0){
					Run.player.turn("left");
					turnCooldown = 10;
				}
				break;
			case 40:
				Op.add("X: " + retTranslate()[0]);
				Op.add("Y: " + retTranslate()[1]);
				Op.dp();
				break;
			case 39:
				if(turnCooldown <= 0){
					Run.player.turn("right");
					turnCooldown = 10;
				}
				break;
		}
	}
	public void keyTyped(KeyEvent k){
		
	}
	public void keyReleased(KeyEvent k){
		switch(k.getKeyCode()){
			case 38:
				Run.player.setMoving(false);
				break;
		}
	}
	ActionListener update = new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	          Run.player.move();
	          turnCooldown -= 1;
	          repaint();
	      }
	  };
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		b.draw(g);
		g.setColor(Color.red);
		g.fillRect(Run.player.getX(), Run.player.getY(), 100, 100);
		timer = new Timer(1000 / FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
}
