package battle;

import java.awt.Graphics;
import graphics.CustomColors;
import graphics.Sprite;

public class Battlefield {
	private int numRows;
	private int numCols;
	private int tileSize;
	private Battle b;
	private Sprite s;
	
	public Battlefield(){
		numRows = 20;
		numCols = 20;
		tileSize = 70;
		
		CustomColors[] c = {CustomColors.purple, CustomColors.gold};
		int[][] map = {
			{0, 1, 1, 1, 1, 1, 0},
			{1, 1, 2, 2, 1, 1, 1},
			{1, 1, 2, 1, 1, 1, 1},
			{1, 1, 2, 2, 2, 1, 1},
			{1, 1, 1, 1, 2, 1, 1},
			{1, 1, 1, 2, 2, 1, 1},
			{0, 1, 1, 1, 1, 1, 0}
		};
		s = new Sprite(c, map);
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
		int x = 0;
		int y = 0;
		
		while(x < numRows * tileSize){
			y = 0;
			while(y < numCols * tileSize){
				s.draw(g, x, y,  tileSize);
				y += tileSize;
			}
			x += tileSize;
		}
		b.draw(g);
	}
}
