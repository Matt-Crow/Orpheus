package world;

import world.battle.Battle;
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
import util.SerialUtil;
import world.entities.AbstractPlayer;
import world.entities.Projectile;
import world.entities.particles.Particle;

/**
 *
 * @author Matt
 */
public class WorldContent implements Serializable{
    private volatile Team playerTeam;
    private volatile Team aiTeam;
    private volatile Map currentMap;
    private volatile Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    private transient TempWorld temp; // remove once Entities reference TempWorld
    
    public WorldContent(int size){
        playerTeam = new Team("Players", Color.green);
        aiTeam = new Team("AI", Color.red);
        currentMap = new Map(size, size);
        currentMinigame = null;
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
    
    public final void setMinigame(Battle b){
        if(b == null){
            throw new NullPointerException();
        }
        currentMinigame = b;
        b.setHost(this);
    }
    public final Battle getGame(){
        return currentMinigame;
    }
    
    public final void init(){
        if(currentMap != null){
            currentMap.init();
        }
        if(currentMinigame != null){
            currentMinigame.init();
        }
    }
    
    public final void update(){
        if(currentMinigame != null){
            currentMinigame.update();
        }
        updateTeam(playerTeam);
        updateTeam(aiTeam);
    }
    
    private void updateTeam(Team t){
        t.update();
        t.forEach((AbstractEntity member)->{
            getMap().checkForTileCollisions(member);
            if(t.getEnemy() != null){
                t.getEnemy().getMembersRem().forEach((AbstractPlayer enemy)->{
                    if(member instanceof Projectile){
                        // I thought that java handled this conversion?
                        ((Projectile)member).checkForCollisions(enemy);
                    }
                    //member.checkForCollisions(enemy);
                });
            }
        });
    }
    
    public final void draw(Graphics g){
        if(currentMap != null){
            currentMap.draw(g);
        }
        playerTeam.draw(g);
        aiTeam.draw(g);
    }
    
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
    
    /**
     * Serializes this using ObjectOutputStream.writeObject,
     * then converts the result to a string so that it can
     * be used as the body of a ServerMessage
     * 
     * This has a problem where if there is too much data
     * in the world, such as in a 99 vs 99, it cannot be encoded
     * 
     * @return the serialized version of this AbstractWorldShell,
 converted to a string for convenience.
     * 
     * @see SerialUtil#serializeToString
     */
    public String serializeToString(){
        return SerialUtil.serializeToString(this);
    }
    
    /**
     * Converts a String generated from AbstractWorldShell.serializeToString,
 and de-serializes it into a copy of that original world.
     * It is important to note that the AbstractWorldShell's canvas is not serialized,
 so be sure to call either setCanvas, or createCanvas so that the AbstractWorldShell
 can be rendered
     * 
     * @param s a string variant of an object stream serialization of a AbstractWorldShell
     * @return a copy of the world which was serialized into a string
     * 
     * @see SerialUtil#fromSerializedString(java.lang.String) 
     */
    public static WorldContent fromSerializedString(String s){
        return (WorldContent)SerialUtil.fromSerializedString(s);
    }
    
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeObject(playerTeam);
        oos.writeObject(aiTeam);
        oos.writeObject(currentMap);
        oos.writeObject(currentMinigame);
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        playerTeam = (Team)ois.readObject();
        aiTeam = (Team)ois.readObject();
        currentMap = (Map)ois.readObject();
        currentMinigame = (Battle)ois.readObject();
    }
}
