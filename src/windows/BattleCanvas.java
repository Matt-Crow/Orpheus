package windows;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import battle.Battlefield;
import initializers.Run;

public class BattleCanvas extends JPanel{
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
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		b.draw(g);
	}
}
