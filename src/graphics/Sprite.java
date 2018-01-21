package graphics;
import java.awt.Graphics;

public class Sprite {
	/*
	 * WARNING: SLOW
	 */
	private CustomColors[] colors;
	private int[][] map;
	
	public Sprite(CustomColors[] c, int[][] m){
		colors = new CustomColors[c.length + 1];
		colors[0] = CustomColors.black;
		for(int i = 0; i < c.length; i++){
			colors[i+1] = c[i];
		}
		map = m;
	}
	public void draw(Graphics g, int x, int y, int s){
		for(int row = 0; row < map.length; row++){
			for(int column = 0; column < map[row].length; column++){
				int xCoord = x + column * s / map[row].length;
				int yCoord = y + row * s / map[column].length;
				
				if(map[row][column] != 0){
					g.setColor(colors[map[row][column]]);
					g.fillRect(xCoord, yCoord, s / map[row].length, s / map[column].length);
				}
			}
		}
	}
}
