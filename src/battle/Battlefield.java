package battle;

import java.awt.Graphics;
import java.awt.Color;

public class Battlefield {
	private int numRows;
	private int numCols;
	private int tileSize;
	private Battle b;
	
	public Battlefield(){
		numRows = 20;
		numCols = 20;
		tileSize = 100;
	}
	public int getTileSize(){
		return tileSize;
	}
	public int getWidth(){
		return numRows * tileSize;
	}
	public int getHeight(){
		return numCols * tileSize;
	}
	public void setHosted(Battle battle){
		b = battle;
	}
	public int[] getCenter(){
		int[] ret = new int[2];
		ret[0] = tileSize * numRows / 2;
		ret[1] = tileSize * numCols / 2;
		return ret;
	}
	public void draw(Graphics g){
		g.setColor(Color.blue);
		int x = 0;
		int y = 0;
		int squareSize = (int) (tileSize * 0.9);
		
		while(x < numRows * tileSize){
			y = 0;
			while(y < numCols * tileSize){
				g.fillRect(x + tileSize / 20, y + tileSize / 20,  squareSize, squareSize);
				y += tileSize;
			}
			x += tileSize;
		}
		b.draw(g);
	}
}
