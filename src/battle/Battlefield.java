package battle;

import java.awt.Graphics;
import java.awt.Color;

public class Battlefield {
	int w;
	int h;
	int s;
	public Battlefield(){
		w = 20;
		h = 20;
		s = 100;
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
	}
}
