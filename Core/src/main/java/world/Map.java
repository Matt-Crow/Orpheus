package world;

import controls.ai.*;
import orpheus.core.world.graph.Graphable;

import static world.Tile.TILE_SIZE;
import world.entities.AbstractEntity;
import java.awt.*;
import java.io.*;
import static java.lang.System.out;
import java.util.*;
import javax.json.*;
import serialization.*;

/**
 * The Map class is used to store Tiles together to form 
 * a 2-dimensional playing field for players to play on.
 * @author Matt
 */                       // todo rm Serializable, JsonSerilable
public class Map implements Serializable, JsonSerialable, Graphable{
    private final int width; //in tiles
    private final int height;
    private final int[][] tileMap;
    //the base Tiles that are copied to construct the map
    private final HashMap<Integer, Tile> tileSet;
    private final ArrayList<Tile> tangibleTiles;
    
    public Map(int w, int h){
        width = w;
        height = h;
        tileMap = new int[h][w];
        for(int i = 0; i < w; i++){
            for(int j = 0; j < h; j++){
                tileMap[j][i] = 0;
            }
        }
        tileSet = new HashMap<>();
        tangibleTiles = new ArrayList<>();
    }
    
    /**
     * Converts this' tile map to a csv string
     * @return the tile map in CSV format
     */
    public String getCsv(){
        StringBuilder b = new StringBuilder();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                b.append(Integer.toString(tileMap[y][x]));
                if(x == width - 1){
                    // on the last one
                    b.append('\n');
                } else {
                    b.append(",");
                }
            }
        }
        return b.toString();
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
     * 
     * @return a deep copy of this' tile set
     */
    public HashMap<Integer, Tile> getTileSet(){
        HashMap<Integer, Tile> ret = new HashMap<>();
        tileSet.entrySet().forEach((e) -> {
            ret.put(e.getKey(), e.getValue().copy(0, 0));
        });
        return ret;
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
        tileMap[yIdx][xIdx] = value;
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
     * Verifies that a given pair of array indexes
     * is within the map array.
     * @param x the x coordinate of the point to verify
     * @param y the y coordinate of the point to verify
     * @return whether or not the given coordinates are within the map
     */
    public boolean isValidIndex(int x, int y){
        return x >= 0 && y >= 0 && x < width && y < height;
    }
    
    /**
     * Checks to see if the given point is on the map,
     * and contains a non-blocking tile.
     * 
     * @param xCoord
     * @param yCoord
     * @return 
     */
    public boolean isOpenTile(int xCoord, int yCoord){
        return isValidIndex(xCoord / Tile.TILE_SIZE, yCoord / Tile.TILE_SIZE)
            && tileSet.containsKey(tileMap[yCoord / Tile.TILE_SIZE][xCoord / Tile.TILE_SIZE])
            && !tileSet.get(tileMap[yCoord / Tile.TILE_SIZE][xCoord / Tile.TILE_SIZE]).getBlocking();
    }
    
    /**
     * Initialized the map,
     * generating the tiles
     */
    public void init(){
        tangibleTiles.clear();
        Tile t;
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(tileSet.containsKey(tileMap[y][x])){
                    t = tileSet.get(tileMap[y][x]).copy(x, y);
                    if(t.getBlocking()){
                        tangibleTiles.add(t);
                    }
                } else {
                    System.out.println("Tile set does not contain key " + tileMap[y][x]);
                }
            }
        }
    }
    
    
    public void checkForTileCollisions(AbstractEntity e){
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
        int t = Tile.TILE_SIZE;
        double diag = Math.sqrt(2) * t;
        
        //check to make sure the points are inside the world
        if(!isValidIndex(x1 / t, y1 / t) || !isValidIndex(x2 / t, y2 / t)){
            return ret;
        }
        
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
        int destX = x2 / t;
        int destY = y2 / t;
        
        //these are array indexes
        int minX = 0;
        int minY = 0;
        int maxX = width - 1;
        int maxY = height - 1;
        
        //make sure the source and destination are intanglible
        if(!isOpenTile(x1, y1) || !isOpenTile(x2, y2)){
            return ret;
        }
        
        PathInfo p = new PathInfo((int)((currXIdx + 0.5) * t), (int)((currYIdx + 0.5) * t), (int)((currXIdx + 0.5) * t), (int)((currYIdx + 0.5)* t), 0);
        stack.push(p);
        visited[currXIdx][currYIdx] = true;
        boolean canUp;
        boolean canDown;
        boolean canLeft;
        boolean canRight;
        int currX;
        int currY;
        
        try{
            while(currXIdx != destX || currYIdx != destY){
                //out.println("Currently at (" + currXIdx + ", " + currYIdx + ")");
                //push adjacent points to the heap
                canUp = currYIdx > minY && tileMap[currYIdx - 1][currXIdx] == 0 && !visited[currXIdx][currYIdx - 1];
                canDown = currYIdx < maxY && tileMap[currYIdx + 1][currXIdx] == 0 && !visited[currXIdx][currYIdx + 1];
                canLeft = currXIdx > minX && tileMap[currYIdx][currXIdx - 1] == 0 && !visited[currXIdx - 1][currYIdx];
                canRight = currXIdx < maxX && tileMap[currYIdx][currXIdx + 1] == 0 && !visited[currXIdx + 1][currYIdx];
                currX = (int)((currXIdx + 0.5) * t);
                currY = (int)((currYIdx + 0.5) * t);
                
                if(canLeft){
                    //the 0.5 shift is to account for the Idx's being the upper left corner of a tile instead of its center 
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx - 0.5) * t), // minus is because - 1 + 0.5
                        currY, 
                        t + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canRight){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx + 1.5) * t), 
                        currY, 
                        t + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canUp){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        currX, 
                        (int)((currYIdx - 0.5) * t), 
                        t + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canDown){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        currX, 
                        (int)((currYIdx + 1.5) * t), 
                        t + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canUp && canLeft && tileMap[currYIdx - 1][currXIdx - 1] == 0){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx - 0.5) * t),
                        (int)((currYIdx - 0.5) * t), 
                        diag + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canUp && canRight && tileMap[currYIdx - 1][currXIdx + 1] == 0){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx + 1.5) * t),
                        (int)((currYIdx - 0.5) * t), 
                        diag + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canDown && canLeft && tileMap[currYIdx + 1][currXIdx - 1] == 0){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx - 0.5) * t),
                        (int)((currYIdx + 1.5) * t), 
                        diag + stack.peek().getAccumDist()
                    );
                    heap.siftUp(p);
                }
                if(canDown && canRight && tileMap[currYIdx + 1][currXIdx + 1] == 0){
                    p = new PathInfo(
                        currX, 
                        currY, 
                        (int)((currXIdx + 1.5) * t),
                        (int)((currYIdx + 1.5) * t), 
                        diag + stack.peek().getAccumDist()
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
            double accuDist = stack.peek().getAccumDist();
            while(!stack.empty()){
                p = stack.pop();
                if(
                    p.getEndX() == (currXIdx + 0.5) * t 
                    && p.getEndY() == (currYIdx + 0.5) * t 
                    && Math.abs(accuDist - p.getAccumDist()) < 0.001
                    && p.getAccumDist() != 0 //don't include the start to start point
                ){
                    ret.push(p); //need to add to front, as the stack is backwards
                    currXIdx = p.getStartX() / t;
                    currYIdx = p.getStartY() / t;
                    accuDist -= p.getDist();
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
    
    public final Path findPath(AbstractEntity from, AbstractEntity to){
        return findPath(from.getX(), from.getY(), to.getX(), to.getY());
    }
    
    
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        for(int i = 0; i < width; ++i){
            for(int j = 0; j < height; ++j){
                tileSet.get(tileMap[j][i]).drawAt(g, i * TILE_SIZE, j * TILE_SIZE);
            }
        }
    }

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", "map");
        b.add("tile map", getCsv());
        JsonObjectBuilder tileSetBuilder = Json.createObjectBuilder();
        tileSet.entrySet().stream().forEach((e)->{
            tileSetBuilder.add(e.getKey().toString(), e.getValue().serializeJson());
        });
        b.add("tile set", tileSetBuilder.build());
        return b.build();
    }
    
    public static Map deserializeJson(JsonObject obj) throws IOException{
        JsonUtil.verify(obj, "tile map");
        JsonUtil.verify(obj, "tile set");
        InputStream in = new ByteArrayInputStream(obj.getString("tile map").getBytes());
        Map ret = MapLoader.readCsv(in);
        
        //deserialize tile set
        JsonObject tileSet = obj.getJsonObject("tile set");
        tileSet.entrySet().forEach((e)->{
            ret.addToTileSet(Integer.parseInt(e.getKey()), Tile.deserializeJson((JsonObject)e.getValue()));
        });
        return ret;
    }

    @Override
    public orpheus.core.world.graph.Map toGraph() {
        var g = new orpheus.core.world.graph.Map(width, height);
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                g.setTile(x, y, tileMap[y][x]);
            }
        }

        for (var kv : tileSet.entrySet()) {
            g.addToTileSet(kv.getKey(), kv.getValue().toGraph());
        }
        return g;
    }
}
