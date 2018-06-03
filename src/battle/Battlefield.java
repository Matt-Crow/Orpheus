package battle;

import java.awt.Graphics;
import graphics.CustomColors;

//TODO: merge with battle
public class Battlefield {
	private int numRows;
	private int numCols;
	private int tileSize;
	
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
	public int[] getCenter(){
		int[] ret = new int[2];
		ret[0] = tileSize * numRows / 2;
		ret[1] = tileSize * numCols / 2;
		return ret;
	}
	
	//make this only draw on-screen chunks
	public void draw(Graphics g){
		int row = 0;
		int col = 0;
		int s = (int)(tileSize * 0.9);
		int spacing = (int)(tileSize * 0.05);
		while(row < numRows){
			col = 0;
			while(col < numCols){
				g.setColor(CustomColors.blue);
				g.fillRect(row * tileSize + spacing, col * tileSize + spacing, s, s);
				col += 1;
			}
			row += 1;
		}
	}
}
