package battle;

import java.awt.Graphics;
import java.awt.Color;

public class Battlefield {
	int w;
	int h;
	int s;
	public Battlefield(int width, int height, int size){
		w = width;
		h = height;
		s = size;
	}
	public void draw(Graphics g){
		g.setColor(Color.blue);
		int x = 0;
		int y = 0;
		int squareSize = (int) (s * 0.9);
		while(x < w){
			while(y < h){
				g.fillRect(x * s + s / 20, y * s + s / 20,  squareSize, squareSize);
				y += 1;
			}
			x += 1;
			y = 0;
		}
	}
}
