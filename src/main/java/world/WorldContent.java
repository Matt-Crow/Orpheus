package world;

import world.battle.Team;
import world.entities.AbstractEntity;
import gui.graphics.Map;
import gui.graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import util.Random;
import world.entities.Projectile;
import world.entities.particles.Particle;
import world.game.Game;

/**
 *
 * @author Matt
 */
public class WorldContent implements Serializable{
    // do I need these volatiles? Final?
    private volatile Team playerTeam;
    private volatile Team aiTeam;
    private volatile Map currentMap;
    private volatile Game game;
    
    private transient TempWorld temp; // remove once Entities reference TempWorld
    
    
    public WorldContent(int size, Game game){
        playerTeam = new Team("Players", Color.green);
        aiTeam = new Team("AI", Color.red);
        currentMap = new Map(size, size);
        this.game = game;
    }
    
    public void setTempWorld(TempWorld temp){
        this.temp = temp;
    }
    
    public void spawn(Particle p){
        temp.spawn(p);
    }
    
    
    
    public final void setPlayerTeam(Team t){
        playerTeam = t;
    }
    
    public final Team getPlayers(){
        return playerTeam;
    }
    
    public final void setAITeam(Team t){
        aiTeam = t;
    }
    public final Team getAi(){
        return aiTeam;
    }
    
    public final void setMap(Map m){
        currentMap = m;
    }
    public final Map getMap(){
        return currentMap;
    }
    
    /*
    public final void setMinigame(Battle b){
        if(b == null){
            throw new NullPointerException();
        }
        game = b;
        b.setHost(this);
    }*/
    
       
    
    // used by Team
    public final void spawnIntoWorld(AbstractEntity e){
        int minX = 0;
        int maxX = currentMap.getWidth() / Tile.TILE_SIZE;
        int minY = 0;
        int maxY = currentMap.getHeight() / Tile.TILE_SIZE;
        int rootX = 0;
        int rootY = 0;
        
        do{
            rootX = Random.choose(minX, maxX);
            rootY = Random.choose(minY, maxY);
        } while(!currentMap.isOpenTile(rootX * Tile.TILE_SIZE, rootY * Tile.TILE_SIZE));
        
        e.setX(rootX * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
        e.setY(rootY * Tile.TILE_SIZE + Tile.TILE_SIZE / 2);
    }
    
    // do I need this anymore?
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeObject(playerTeam);
        oos.writeObject(aiTeam);
        oos.writeObject(currentMap);
        oos.writeObject(game);
    }
    
    // do I need this anymore?
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        playerTeam = (Team)ois.readObject();
        aiTeam = (Team)ois.readObject();
        currentMap = (Map)ois.readObject();
        game = (Game)ois.readObject();
    }


    
    public final Game getGame(){
        return game;
    }
    
    protected void init(){
        currentMap.init();
        game.play();
    } 
    
    protected void update(){
        playerTeam.update();
        aiTeam.update();
        game.update();
        
        checkForCollisions(playerTeam, aiTeam);
        checkForCollisions(aiTeam, playerTeam);
    }
    
    private void checkForCollisions(Team t1, Team t2) {
        t1.forEach((e)->{
            currentMap.checkForTileCollisions(e);
            if(e instanceof Projectile){
                t2.getMembersRem().forEach((p)->{
                    ((Projectile) e).checkForCollisions(p);
                });
            }
        });
    }
    
    public void draw(Graphics g){
        currentMap.draw(g);
        playerTeam.draw(g);
        aiTeam.draw(g);
    }
}
