package graphics;

import entities.Entity;
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
    private static final Color TANGIBLE_OUTLINE_COLOR = Color.BLACK; //the color of the outline around tiles that check collisions
    private static final Color INTANGIBLE_OUTLINE_COLOR = Color.WHITE; //the color of the outline around tiles that don't check collisions
    
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
    
    //each subclass will have to override
    public Tile copy(int x, int y){
        Tile ret = new Tile(x, y, c);
        ret.blocking = blocking;
        return ret;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public final boolean contains(Entity e){
        return e.isWithin(x, y, TILE_SIZE, TILE_SIZE);
    }
    
    public void setBlocking(boolean blocksEntities){
        blocking = blocksEntities;
    }
    
    public boolean getBlocking(){
        return blocking;
    }
    
    public void draw(Graphics g){
        if(blocking){
            g.setColor(TANGIBLE_OUTLINE_COLOR);
        } else {
            g.setColor(INTANGIBLE_OUTLINE_COLOR);
        }
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
        g.setColor(c);
        g.fillRect(x + TILE_SPACING, y + TILE_SPACING, RECT_SIZE, RECT_SIZE);
    }
}
