package graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Tiles are rendered whenever World.draw is invoked
 * @author Matt Crow
 */
public class Tile {
    public static final int TILE_SIZE = 100;
    private static final int TILE_SPACING = (int)(TILE_SIZE * 0.05);
    private static final int RECT_SIZE = (int)(TILE_SIZE * 0.9);
    
    private final int x;
    private final int y;
    
    private final Color c;
    private boolean blocking; //Entities cannot move through
    
    /**
     * 
     * @param xIdx the index of the Tile in the x axis of the World
     * @param yIdx the index of the Tile in the y axis of the World
     * @param color 
     */
    public Tile(int xIdx, int yIdx, Color color){
        x = xIdx * TILE_SIZE;
        y = yIdx * TILE_SIZE;
        c = color;
        blocking = false;
    }
    
    public void setBlocking(boolean blocksEntities){
        blocking = blocksEntities;
    }
    
    public void draw(Graphics g){
        if(blocking){
            g.setColor(Color.BLACK);
            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
            g.setColor(c);
            g.fillRect(x + TILE_SPACING, y + TILE_SPACING, RECT_SIZE, RECT_SIZE);
        } else {
            g.setColor(c);
            g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        }
    }
}
