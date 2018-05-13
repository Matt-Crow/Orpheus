package battle;

import java.awt.Graphics;
import graphics.CustomColors;
import resources.Op;

public class Battlefield {
	private int numRows;
	private int numCols;
	private int tileSize;
	private int chunkSize;
	private Chunk firstChunk;
	private Battle b;
	
	public Battlefield(){
		numRows = 20;
		numCols = 20;
		tileSize = 100;
		
		chunkSize = 500;
		
		firstChunk = new Chunk(-1, -1, chunkSize);
		Chunk next = firstChunk;
		for(int i = 0; i * chunkSize < numRows * tileSize; i++){
			for(int j = 0; j * chunkSize < numCols * tileSize; j++){
				next = next.spawn(i * chunkSize, j * chunkSize);
			}
		}
		next = firstChunk;
		while(next.getHasNext()){
			next = next.getNext();
			Op.add("X: " + next.getX());
			Op.add("Y: " + next.getY());
			Op.dp();
		}
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
	public Chunk getChunkContaining(int x, int y){
		Chunk ret = firstChunk;
		boolean found = false;
		while(ret.getHasNext() && !found){
			ret = ret.getNext();
			found = ret.contains(x, y);
		}
		return ret;
	}
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
		b.draw(g);
		Chunk current = firstChunk;
		while(current.getHasNext()){
			current = current.getNext();
			current.draw(g);
		}
	}
	public void updateAllChunks(){
		Chunk current = firstChunk;
		while(current.getHasNext()){
			current = current.getNext();
			current.update();
		}
	}
}
