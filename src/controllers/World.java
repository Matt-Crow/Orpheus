package controllers;

import ai.Path;
import ai.PathInfo;
import ai.PathMinHeap;
import battle.Team;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.out;
import java.util.Stack;

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
        return this;
    }
    
    public void initTiles(){
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
     * Parameters are the coordinates, NOT indexes in the map array
     * @param x1 the x coordinate of the starting point
     * @param y1 the y coordinate of the starting point
     * @param x2
     * @param y2 
     * @return  
     */
    public Path findPath(int x1, int y1, int x2, int y2){
        Path ret = new Path();
        boolean[][] visited = new boolean[worldSize][worldSize];
        for(int i = 0; i < worldSize; i++){
            for(int j = 0; j < worldSize; j++){
                visited[i][j] = false;
            }
        }
        
        PathMinHeap heap = new PathMinHeap(worldSize * worldSize);
        Stack<PathInfo> stack = new Stack<>();
        int currX = x1 / Tile.TILE_SIZE; //array indexes
        int currY = y1 / Tile.TILE_SIZE;
        int destX = x2 / Tile.TILE_SIZE;
        int destY = y2 / Tile.TILE_SIZE;
        
        //first, bind the search to within a small region so that the algorithm doesn't search everywhere
        int minX = currX - 3; //temporary until I can find a better way
        int minY = currY - 3;
        int maxX = currX + 3;
        int maxY = currY + 3;
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
        
        PathInfo p = new PathInfo(currX, currY, currX, currY, 0);
        stack.push(p);
        visited[currX][currY] = true;
        try{
            while(currX != destX || currY != destY){
                out.println("Currently at (" + currX + ", " + currY + ")");
                    //push adjacent points to the heap
                    if(currX > minX && map[currX - 1][currY] == 0 && !visited[currX - 1][currY]){
                        //can go left
                        p = new PathInfo(currX, currY, currX - 1, currY, 1 + stack.peek().getDist());
                        heap.siftUp(p);
                    }
                    if(currX < maxX && map[currX + 1][currY] == 0 && !visited[currX + 1][currY]){
                        //can go right
                        p = new PathInfo(currX, currY, currX + 1, currY, 1 + stack.peek().getDist());
                        heap.siftUp(p);
                    }
                    if(currY > minY && map[currX][currY - 1] == 0 && !visited[currX][currY - 1]){
                        //can go up
                        p = new PathInfo(currX, currY, currX, currY - 1, 1 + stack.peek().getDist());
                        heap.siftUp(p);
                    }
                    if(currY < maxY && map[currX][currY + 1] == 0 && !visited[currX][currY + 1]){
                        //can go down
                        p = new PathInfo(currX, currY, currX, currY + 1, 1 + stack.peek().getDist());
                        heap.siftUp(p);
                    }
                    //heap.print();

                    do{
                        p = heap.siftDown();
                    } while(visited[p.getEndX()][p.getEndY()]);
                    stack.push(p);
                    currX = p.getEndX();
                    currY = p.getEndY();
                    visited[currX][currY] = true;
            }
            double accuDist = stack.peek().getDist();
            while(!stack.empty()){
                p = stack.pop();
                if(p.getEndX() == currX && p.getEndY() == currY && accuDist == p.getDist()){
                    ret.add(p);
                    currX = p.getStartX();
                    currY = p.getStartY();
                    accuDist -= 1;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        
        return ret;
    }
    
    
    
    
    
    public void update(){
        teams.forEach((Team t)->{
            t.update();
            t.forEach((Entity member)->{
                checkForTileCollisions(member);
                t.getEnemy().getMembersRem().forEach((Player enemy)->{
                    if(member instanceof Projectile){
                        // I thought that java handled this conversion?
                        ((Projectile)member).checkForCollisions(enemy);
                    }
                    //member.checkForCollisions(enemy);
                });
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
