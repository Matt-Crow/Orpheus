package battle;

import java.awt.Graphics;
import java.awt.Color;
import initializers.Run;

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
		
		int x = Run.player.getX() - (s * w / 2);
		int y = Run.player.getY() - (s * h / 2);
		int squareSize = (int) (s * 0.9);
		
		while(x <= w * s){
			while(y <= h * s){
				g.fillRect(x + s / 20, y + s / 20,  squareSize, squareSize);
				y += s;
			}
			x += s;
			y = 0;
		}
	}
}
