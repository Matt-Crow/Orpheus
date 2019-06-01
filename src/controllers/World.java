package controllers;

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
     */
    public void findPath(int x1, int y1, int x2, int y2){
        int arrSize = worldSize * worldSize; //convert the two-d array of the world to a one-d to make it easier to work with
        
        boolean[] visited = new boolean[arrSize];
        double[] distances = new double[arrSize];
        int[] closest = new int[arrSize];
        
        Arrays.fill(visited, false);
        Arrays.fill(distances, Double.MAX_VALUE);
        Arrays.fill(closest, -1);
        
        /*
        if (x, 0) corresponds to index x, given x < worldSize,
        then (x, y) corrsponds to index x + y * worldSize
        since neither x nor y can never equal the worldsize (index out of bounds),
        the maximum index is (worldSize - 1) + (worldSize^2 - worldSize) = worldSize^2 - 1
        */
        int curr = x1 / Tile.TILE_SIZE + (y1 / Tile.TILE_SIZE) * worldSize;
        int currX = x1 / Tile.TILE_SIZE; //array indexes
        int currY = y1 / Tile.TILE_SIZE;
        
        int dest = x2 / Tile.TILE_SIZE + (y2 / Tile.TILE_SIZE) * worldSize;
        
        while(curr != dest){
            out.println("Currently at " + curr);
            if(currX > 0 && map[currX - 1][currY] == 0){
                //can go left
            }
            if(currX < worldSize && map[currX + 1][currY] == 0){
                //can go right
            }
            if(currY > 0 && map[currX][currY - 1] == 0){
                //can go up
            }
            if(currY < worldSize && map[currX][currY + 1] == 0){
                //can go down
            }
            break;
        }
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
