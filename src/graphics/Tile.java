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
    
    public Color getColor(){
        return c;
    }
    
    public final boolean contains(Entity e){
        return e.isWithin(x, y, TILE_SIZE, TILE_SIZE);
    }
    
    public void shoveOut(Entity e){
        
        //first, check to see which kind of collision to check for
        //if I shove from both axis, results in entities acting strangely with blocks
        int withinX = Math.abs(x + TILE_SIZE / 2 - e.getX());
        int withinY = Math.abs(y + TILE_SIZE / 2 - e.getY());
        boolean moreX = withinX > withinY;
        
        if(moreX && e.getX() + e.getRadius() > x && e.getX() < x + TILE_SIZE / 2){
            //left collide
            e.setX(x - e.getRadius());
        } else if(moreX && e.getX() - e.getRadius() > x + TILE_SIZE / 2 && e.getX() - e.getRadius() < x + TILE_SIZE){
            //right collide
            e.setX(x + TILE_SIZE + e.getRadius());
        }
        
        if(!moreX && e.getY() + e.getRadius() > y && e.getY() < y + TILE_SIZE / 2){
            //top collide
            e.setY(y - e.getRadius());
        } else if(!moreX && e.getY() - e.getRadius() > y + TILE_SIZE / 2 && e.getY() - e.getRadius() < y + TILE_SIZE){
            //bottom collide
            e.setY(y + TILE_SIZE + e.getRadius());
        }
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
