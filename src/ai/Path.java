package ai;

import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

/**
 *
 * @author Matt
 */
public class Path {
    private LinkedList<PathInfo> path;
    public Path(){
        path = new LinkedList<>();
    }
    
    public void add(PathInfo p){
        path.add(p);
    }
    
    public void draw(Graphics g){
        g.setColor(Color.red);
        for(PathInfo p : path){
            g.fillRect(p.getStartX() * Tile.TILE_SIZE, p.getStartY() * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
        }
    }
}
