package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import battle.Battle;
import battle.Battlefield;
import entities.Player;
import resources.Op;

public class BattleCanvas extends JPanel implements KeyListener{
	public static final long serialVersionUID = 1L;
	Battlefield b;
	Battle hostedBattle;
	private int w;
	private int h;
	private Timer timer;
	private int FPS;
	Player p;
	ActionListener update;
	
	public BattleCanvas(int windowWidth, int windowHeight){
		w = windowWidth;
		h = windowHeight;
		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(this);
		FPS = 20;
		
		hostedBattle = new Battle();
		b = new Battlefield();
		
		hostedBattle.setHost(b);
		hostedBattle.init();
		p = hostedBattle.getPlayer();
		
		update = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          hostedBattle.update();
		          repaint();
		      }
		};
	}
	
	public int[] retTranslate(){
		int[] ret = new int[2];
		int x = -p.getX() + w / 2;
		int y = -p.getY() + h / 2;
		int minX = -(b.getWidth() - w);
		int minY = -(b.getHeight() - h);
		
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
		
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	public void keyPressed(KeyEvent k){
		//out.print("Pressed: ");
		//out.println(k.getKeyCode());
		switch(k.getKeyCode()){
			case 38:
				p.setMoving(true);
				break;
			case 37:
				p.turn("left");
				break;
			case 40:
				Op.add("X: " + retTranslate()[0]);
				Op.add("Y: " + retTranslate()[1]);
				Op.dp();
				break;
			case 39:
				p.turn("right");
				break;
		}
	}
	public void keyTyped(KeyEvent k){
		
	}
	public void keyReleased(KeyEvent k){
		switch(k.getKeyCode()){
			case 38:
				p.setMoving(false);
				break;
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		b.draw(g);
		timer = new Timer(1000 / FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
}
