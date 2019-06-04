package graphics;

import ai.Path;
import ai.PathInfo;
import ai.PathMinHeap;
import entities.Entity;
import java.awt.Graphics;
import java.awt.Color;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * The Map class is used to store Tiles together to form 
 * a 2-dimensional playing field for players to play on.
 * @author Matt
 */
public class Map {
    private final int width; //in tiles
    private final int height;
    private final int[][] tileMap;
    //the base Tiles that are copied to construct the map
    private final HashMap<Integer, Tile> tileSet;
    private final ArrayList<Tile> allTiles;
    private final ArrayList<Tile> tangibleTiles;
    
    public Map(int w, int h){
        width = w;
        height = h;
        tileMap = new int[w][h];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                tileMap[i][j] = 0;
            }
        }
        tileSet = new HashMap<>();
        allTiles = new ArrayList<>();
        tangibleTiles = new ArrayList<>();
    }
    
    /**
     * Adds a Tile to this' set.
     * Any array indexes with a 
     * value equal to the first parameter will
     * generate the given Tile upon initializing tiles
     * @param valueInMap the key for this tile
     * @param t the Tile to copy when generating the map.
     * @return this, for chaining purposes
     */
    public Map addToTileSet(int valueInMap, Tile t){
        tileSet.put(valueInMap, t);
        return this;
    }
    
    /**
     * Sets a tile in this' tile map to a given value.
     * for example, calling setTile(1, 0, 2) will cause
     * the tile at x index 1, y index 0, to become the type
     * of tile with a key of 2 in this' tile set.
     * @param xIdx
     * @param yIdx
     * @param value
     * @return 
     */
    public Map setTile(int xIdx, int yIdx, int value){
        if(xIdx < 0 || xIdx >= width || yIdx < 0 || yIdx >= height){
            throw new IndexOutOfBoundsException();
        }
        tileMap[xIdx][yIdx] = value;
        return this;
    }
    
    /**
     * 
     * @return the width of this map, in pixels
     */
    public int getWidth(){
        return Tile.TILE_SIZE * width;
    }
    
    /**
     * 
     * @return the height of the map, in pixels 
     */
    public int getHeight(){
        return Tile.TILE_SIZE * height;
    }
    
    /**
     * Initialized the map,
     * generating the tiles
     */
    public void init(){
        allTiles.clear();
        tangibleTiles.clear();
        Tile t;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(tileSet.containsKey(tileMap[x][y])){
                    t = tileSet.get(tileMap[x][y]).copy(x, y);
                    allTiles.add(t);
                    if(t.getBlocking()){
                        tangibleTiles.add(t);
                    }
                } else {
                    System.out.println("Tile set does not contain key " + tileMap[x][y]);
                }
            }
        }
    }
    
    
    public void checkForTileCollisions(Entity e){
        //make sure the entity is within the world
        if(e.getX() - e.getRadius() < 0){
            //outside of left bound
            e.setX(e.getRadius());
        } else if(e.getX() + e.getRadius() > getWidth()){
            //right
            e.setX(getWidth() - e.getRadius());
        }
        
        if(e.getY() - e.getRadius() < 0){
            //top
            e.setY(e.getRadius());
        } else if(e.getY() + e.getRadius() > getHeight()){
            //bottom
            e.setY(getHeight() - e.getRadius());
        }
        
        tangibleTiles.forEach((Tile t)->{
            if(t.contains(e)){
                t.shoveOut(e);
            }
        });
    }
    
    /**
     * Dijkstra's algorithm.
     * Parameters are the coordinates, NOT indexes in the map array
     * Currently, the only tiles this can find a path on is 0, 
     * but I can add checking to see if the tile is tangible later
     * 
     * @param x1 the x coordinate of the starting point
     * @param y1 the y coordinate of the starting point
     * @param x2
     * @param y2 
     * @return the path between the two points. If none exists, 
     * or an error is encountered, the Path returned will have no info
     */
    public Path findPath(int x1, int y1, int x2, int y2){
        //out.println(String.format("Finding path from (%d, %d) to (%d, %d)", x1, y1, x2, y2));
        
        Path ret = new Path();
        
        //check to make sure the points are inside the world
        int maxW = width * Tile.TILE_SIZE;
        int maxH = height * Tile.TILE_SIZE;
        if(
            x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 ||
            x1 > maxW || y1 > maxH || x2 > maxW || y2 > maxH
        ){
            return ret;
        }
        
        int t = Tile.TILE_SIZE;
        boolean[][] visited = new boolean[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                visited[i][j] = false;
            }
        }
        
        PathMinHeap heap = new PathMinHeap(width * height);
        Stack<PathInfo> stack = new Stack<>();
        int currXIdx = x1 / t; //array indexes
        int currYIdx = y1 / t;
        int destX = x2 / Tile.TILE_SIZE;
        int destY = y2 / Tile.TILE_SIZE;
        
        //these are array indexes
        int minX = 0;
        int minY = 0;
        int maxX = width - 1;
        int maxY = height - 1;
        
        //make sure the source and destination are intanglible
        if(
            currXIdx < minX || currXIdx > maxX
            || currYIdx < minY || currYIdx > maxY
            || destX < minX || destX > maxX
            || destY < minY || destY > maxY
            || tileMap[currXIdx][currYIdx] != 0 
            || tileMap[destX][destY] != 0
        ){
            return ret;
        }
        
        
        
        PathInfo p = new PathInfo((int)((currXIdx + 0.5) * t), (int)((currYIdx + 0.5) * t), (int)((currXIdx + 0.5) * t), (int)((currYIdx + 0.5)* t), 0);
        stack.push(p);
        visited[currXIdx][currYIdx] = true;
        try{
            while(currXIdx != destX || currYIdx != destY){
                //out.println("Currently at (" + currXIdx + ", " + currYIdx + ")");
                //push adjacent points to the heap
                if(currXIdx > minX && tileMap[currXIdx - 1][currYIdx] == 0 && !visited[currXIdx - 1][currYIdx]){
                    //can go left
                    //the 0.5 shift is to account for the Idx's being the upper left corner of a tile instead of its center 
                    p = new PathInfo(
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx + 0.5) * t), 
                        (int)((currXIdx - 0.5) * t), // minus is because - 1 + 0.5
                        (int)((currYIdx + 0.5) * t), 
                        t + stack.peek().getDist()
                    );
                    heap.siftUp(p);
                }
                if(currXIdx < maxX && tileMap[currXIdx + 1][currYIdx] == 0 && !visited[currXIdx + 1][currYIdx]){
                    //can go right
                    p = new PathInfo(
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx + 0.5) * t), 
                        (int)((currXIdx + 1.5) * t), 
                        (int)((currYIdx + 0.5) * t), 
                        t + stack.peek().getDist()
                    );
                    heap.siftUp(p);
                }
                if(currYIdx > minY && tileMap[currXIdx][currYIdx - 1] == 0 && !visited[currXIdx][currYIdx - 1]){
                    //can go up
                    p = new PathInfo(
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx + 0.5) * t), 
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx - 0.5) * t), 
                        t + stack.peek().getDist()
                    );
                    heap.siftUp(p);
                }
                if(currYIdx < maxY && tileMap[currXIdx][currYIdx + 1] == 0 && !visited[currXIdx][currYIdx + 1]){
                    //can go down
                    p = new PathInfo(
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx + 0.5) * t), 
                        (int)((currXIdx + 0.5) * t), 
                        (int)((currYIdx + 1.5) * t), 
                        t + stack.peek().getDist()
                    );
                    heap.siftUp(p);
                }
                //heap.print();
                do{
                    p = heap.siftDown();
                } while(visited[p.getEndX() / t][p.getEndY() / t]);
                stack.push(p);
                currXIdx = p.getEndX() / t;
                currYIdx = p.getEndY() / t;
                visited[currXIdx][currYIdx] = true;
            }
            double accuDist = stack.peek().getDist();
            while(!stack.empty()){
                p = stack.pop();
                if(
                    p.getEndX() == (currXIdx + 0.5) * t 
                    && p.getEndY() == (currYIdx + 0.5) * t 
                    && accuDist == p.getDist()
                    && p.getDist() != 0 //don't include the start to start point
                ){
                    ret.push(p); //need to add to front, as the stack is backwards
                    currXIdx = p.getStartX() / t;
                    currYIdx = p.getStartY() / t;
                    accuDist -= t;
                }
            }
        } catch(Exception e){
            out.println("Error encountered while trying to find the path");
            out.println(String.format("(%d, %d) to (%d, %d)", x1, y1, x2, y2));
            out.println("Stack: ");
            stack.forEach((PathInfo pi)->out.println(pi.toString()));
            out.println("Heap: ");
            heap.print();
            e.printStackTrace();
        }
        return ret;
    }
    
    
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        allTiles.forEach((tile)->tile.draw(g));
    }
}
