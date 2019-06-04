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
import graphics.Map;
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
    private final ArrayList<Team> teams; //makes it faster to find nearest enemies
    //maybe just keep track of everything in one linked list?
    
    private Map currentMap;
    private WorldCanvas canvas;
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    public World(int size){
        teams = new ArrayList<>();
        
        currentMap = new Map(size);
        canvas = null;
        currentMinigame = null;
    }
    
    public final World addTeam(Team t){
        teams.add(t);
        t.forEachMember((Player p)->p.setWorld(this));
        return this;
    }
    
    public World setMap(Map m){
        if(m == null){
            throw new NullPointerException();
        } else {
            currentMap = m;
        }
        return this;
    }
    public Map getMap(){
        return currentMap;
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
    
    //working here
    public boolean spawnIntoWorld(Entity e, int x, int y){
        boolean success = false;
        int t = Tile.TILE_SIZE;
        int s = currentMap.getSize();
        /*
        move all this checking to Map
        for(int yIdx = y / t; yIdx < worldSize && !success; yIdx++){
            for(int xIdx = x / t; xIdx < worldSize && !success; xIdx++){
                
                if(!tileSet.containsKey(map[xIdx][yIdx]) || !tileSet.get(map[xIdx][yIdx]).getBlocking()){
                    success = true;
                    //spawn the entity
                }
            }
        }*/
        
        return success;
    }
    
    public void init(){
        if(currentMap != null){
            currentMap.init();
        }
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
                currentMap.checkForTileCollisions(member);
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
        currentMap.draw(g);
        teams.forEach((t)->t.draw(g));
    }
}
