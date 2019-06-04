package controllers;

import ai.Path;
import ai.PathInfo;
import ai.PathMinHeap;
import battle.Battle;
import battle.Team;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;
import java.util.Stack;
import windows.WorldCanvas;

/**
 * The World class will act as a controller for the game.
 * It will keep track of all Entities in the game through one of 2 ways:
 * either (a): by keeping track of each Team, which in turn keeps track of various entities
 * or (b): Keeping track of all Entities as one gigantic linked list (of my own design).
 * (a) has the advantage of being faster to check for collisions, as team members should collide with one another (or should they?)
 * (b) suits a less structured world where Entities can be unaffilliated with a team, and allows me to search faster if the linked list is sorted by ID.
 * 
 * The World will handle all the drawing and updating as well.
 * @author Matt Crow
 */
public class World {
    private final int worldSize;
    private final int[][] map;
    
    //the tile designs that correspond to a given number int the map.
    //for example, all 1's on the map array correspond to a copy of tileSet.get(1)
    private final HashMap<Integer, Tile> tileSet;
    
    private final ArrayList<Tile> allTiles;
    private final ArrayList<Tile> tangibleTiles;
    
    private final ArrayList<Team> teams; //makes it faster to find nearest enemies
    //maybe just keep track of everything in one linked list?
    
    private WorldCanvas canvas;
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    public World(int size){
        worldSize = size;
        map = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = 0;
            }
        }
        tileSet = new HashMap<>();
        allTiles = new ArrayList<>();
        tangibleTiles = new ArrayList<>();
        teams = new ArrayList<>();
        canvas = null;
        currentMinigame = null;
    }
    
    public World setTile(int x, int y, int value){
        map[x][y] = value;
        return this;
    }
    
    public World setBlock(int valueInMap, Tile t){
        tileSet.put(valueInMap, t);
        return this;
    }
    
    public int getSize(){
        return Tile.TILE_SIZE * worldSize;
    }
    
    public final World addTeam(Team t){
        teams.add(t);
        t.forEachMember((Player p)->p.setWorld(this));
        return this;
    }
    
    private void checkForTileCollisions(Entity e){
        //make sure the entity is within the world
        if(e.getX() - e.getRadius() < 0){
            //outside of left bound
            e.setX(e.getRadius());
        } else if(e.getX() + e.getRadius() > getSize()){
            //right
            e.setX(getSize() - e.getRadius());
        }
        
        if(e.getY() - e.getRadius() < 0){
            //top
            e.setY(e.getRadius());
        } else if(e.getY() + e.getRadius() > getSize()){
            //bottom
            e.setY(getSize() - e.getRadius());
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
        int max = worldSize * Tile.TILE_SIZE;
        if(
            x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 ||
            x1 > max || y1 > max || x2 > max || y2 > max
        ){
            return ret;
        }
        
        int t = Tile.TILE_SIZE;
        boolean[][] visited = new boolean[worldSize][worldSize];
        for(int i = 0; i < worldSize; i++){
            for(int j = 0; j < worldSize; j++){
                visited[i][j] = false;
            }
        }
        
        PathMinHeap heap = new PathMinHeap(worldSize * worldSize);
        Stack<PathInfo> stack = new Stack<>();
        int currXIdx = x1 / t; //array indexes
        int currYIdx = y1 / t;
        int destX = x2 / Tile.TILE_SIZE;
        int destY = y2 / Tile.TILE_SIZE;
        
        //these are array indexes
        int minX = 0;
        int minY = 0;
        int maxX = worldSize - 1;
        int maxY = worldSize - 1;
        if(minX < 0){
            minX = 0;
        }
        if(minY < 0){
            minY = 0;
        }
        if(maxX > worldSize - 1){
            maxX = worldSize - 1;
        }
        if(maxY > worldSize - 1){
            maxY = worldSize - 1;
        }
        
        //make sure the source and destination are intanglible
        if(
            currXIdx < minX || currXIdx > maxX
            || currYIdx < minY || currYIdx > maxY
            || destX < minX || destX > maxX
            || destY < minY || destY > maxY
            || map[currXIdx][currYIdx] != 0 
            || map[destX][destY] != 0
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
                if(currXIdx > minX && map[currXIdx - 1][currYIdx] == 0 && !visited[currXIdx - 1][currYIdx]){
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
                if(currXIdx < maxX && map[currXIdx + 1][currYIdx] == 0 && !visited[currXIdx + 1][currYIdx]){
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
                if(currYIdx > minY && map[currXIdx][currYIdx - 1] == 0 && !visited[currXIdx][currYIdx - 1]){
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
                if(currYIdx < maxY && map[currXIdx][currYIdx + 1] == 0 && !visited[currXIdx][currYIdx + 1]){
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
    
    public void createCanvas(){
        canvas = new WorldCanvas(this);
    }
    
    public void setCanvas(WorldCanvas c){
        canvas = c;
    }
    public WorldCanvas getCanvas(){
        return canvas;
    }
    
    public void setCurrentMinigame(Battle b){
        currentMinigame = b;
    }
    public Battle getCurrentMinigame(){
        return currentMinigame;
    }
    
    private void initTiles(){
        allTiles.clear();
        tangibleTiles.clear();
        Tile t;
        
        for(int x = 0; x < worldSize; x++){
            for(int y = 0; y < worldSize; y++){
                if(tileSet.containsKey(map[x][y])){
                    t = tileSet.get(map[x][y]).copy(x, y);
                    allTiles.add(t);
                    if(t.getBlocking()){
                        tangibleTiles.add(t);
                    }
                }
            }
        }
    }
    
    //working here
    public boolean spawnIntoWorld(Entity e, int x, int y){
        boolean success = false;
        int t = Tile.TILE_SIZE;
        int s = getSize();
        for(int yIdx = y / t; yIdx < worldSize && !success; yIdx++){
            for(int xIdx = x / t; xIdx < worldSize && !success; xIdx++){
                if(!tileSet.containsKey(map[xIdx][yIdx]) || !tileSet.get(map[xIdx][yIdx]).getBlocking()){
                    success = true;
                    //spawn the entity
                }
            }
        }
        
        return success;
    }
    
    public void init(){
        initTiles();
        if(currentMinigame != null){
            currentMinigame.init();
        }
    }
    
    public void update(){
        if(currentMinigame != null){
            currentMinigame.update();
        }
        teams.forEach((Team t)->{
            t.update();
            t.forEach((Entity member)->{
                checkForTileCollisions(member);
                if(t.getEnemy() != null){
                    t.getEnemy().getMembersRem().forEach((Player enemy)->{
                        if(member instanceof Projectile){
                            // I thought that java handled this conversion?
                            ((Projectile)member).checkForCollisions(enemy);
                        }
                        //member.checkForCollisions(enemy);
                    });
                }
            });
        });
    }
    
    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize(), getSize());
        
        allTiles.forEach((tile)->tile.draw(g));
        teams.forEach((t)->t.draw(g));
    }
}
