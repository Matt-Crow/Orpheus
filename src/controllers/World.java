package controllers;

import battle.Battle;
import battle.Team;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import graphics.Tile;
import graphics.Map;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import windows.WorldCanvas;
import static java.lang.System.out;

/**
 * The World class will act as a controller for the game.
 * It keeps track of all Entities in the game by keeping track of each Team, 
 * which in turn keeps track of various entities
 * 
 * The World will handle all the drawing and updating as well.
 * @author Matt Crow
 */
public class World implements Serializable{
    private final ArrayList<Team> teams; //makes it faster to find nearest enemies
    //maybe just keep track of everything in one linked list?
    
    private Map currentMap;
    private transient WorldCanvas canvas; //transient means "don't serialize me!"
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    public World(int size){
        teams = new ArrayList<>();
        
        currentMap = new Map(size, size);
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
    /*
    //working here
    //move all this checking to Map
    public boolean spawnIntoWorld(Entity e, int x, int y){
        boolean success = false;
        int t = Tile.TILE_SIZE;
        int s = currentMap.getSize();
        
        for(int yIdx = y / t; yIdx < worldSize && !success; yIdx++){
            for(int xIdx = x / t; xIdx < worldSize && !success; xIdx++){
                
                if(!tileSet.containsKey(map[xIdx][yIdx]) || !tileSet.get(map[xIdx][yIdx]).getBlocking()){
                    success = true;
                    //spawn the entity
                }
            }
        }
        
        return success;
    }*/
    
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
    
    public void displayData(){
        out.println("WORLD:");
        out.println("Current minigame:");
        //currentMinigame.displayData();
        out.println("Current map:");
        //currentMap.displayData();
        out.println("Teams:");
        teams.forEach((Team t)->{
            t.displayData();
        });
    }
}
