package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import battle.Battlefield;
import initializers.Run;

import static java.lang.System.out;

public class BattleCanvas extends JPanel implements KeyListener{
	public static final long serialVersionUID = 1L;
	Battlefield b;
	private int w;
	private int h;
	private int s;
	
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
		out.print("Pressed: ");
		out.println(k.getKeyCode());
		switch(k.getKeyCode()){
			case 38:
				Run.player.setDirection("N");
				break;
			case 37:
				Run.player.setDirection("W");
				break;
			case 40:
				Run.player.setDirection("S");
				break;
			case 39:
				Run.player.setDirection("E");
				break;
		}
	}
	public void keyTyped(KeyEvent k){
		
	}
	public void keyReleased(KeyEvent k){
		out.print("Released: ");
		out.println(k.getKeyCode());
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		Run.player.move(1);
		b.draw(g);
	}
}
