package battle;

import java.awt.Graphics;
import java.awt.Color;
import initializers.Master;

public class Battlefield {
	private int w;
	private int h;
	private int s;
	private Battle b;
	
	public Battlefield(){
		w = 20;
		h = 20;
		s = Master.CANVASWIDTH / 5;
	}
	public int getTileSize(){
		return s;
	}
	public int getWidth(){
		return w * s;
	}
	public int getHeight(){
		return h * s;
	}
	public void setHosted(Battle battle){
		b = battle;
	}
	public int[] getCenter(){
		int[] ret = new int[2];
		ret[0] = s * w / 2;
		ret[1] = s * h / 2;
		return ret;
	}
	public void draw(Graphics g){
		g.setColor(Color.blue);
		int x = 0;
		int y = 0;
		int squareSize = (int) (s * 0.9);
		
		while(x < w * s){
			y = 0;
			while(y < h * s){
				g.fillRect(x + s / 20, y + s / 20,  squareSize, squareSize);
				y += s;
			}
			x += s;
		}
		b.draw(g);
	}
}
