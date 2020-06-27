package world;

import battle.Battle;
import battle.Team;
import entities.AbstractEntity;
import graphics.CustomColors;
import graphics.Map;
import graphics.MapLoader;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import util.Random;

/**
 *
 * @author Matt
 */
public class WorldContent implements Serializable{
    private volatile Team playerTeam;
    private volatile Team aiTeam;
    private volatile Map currentMap;
    private volatile Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    public WorldContent(int size){
        playerTeam = new Team("Players", Color.green);
        aiTeam = new Team("AI", Color.red);
        currentMap = new Map(size, size);
        currentMinigame = null;
    }
    
    public static WorldContent createDefaultBattle(){
        WorldContent content = new WorldContent(20);
        try {
            content.setMap(MapLoader.readCsv(AbstractWorld.class.getResourceAsStream("/testMap.csv")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Tile block = new Tile(0, 0, CustomColors.GRAY);
        block.setBlocking(true);
        content.currentMap.addToTileSet(0, new Tile(0, 0, Color.BLUE));
        content.currentMap.addToTileSet(1, block);
        
        return content;
    }
    
    public final void setPlayerTeam(Team t){
        playerTeam = t;
    }
    public final Team getPlayerTeam(){
        return playerTeam;
    }
    
    public final void setAITeam(Team t){
        aiTeam = t;
    }
    public final Team getAITeam(){
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
    }
    public final Battle getMinigame(){
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
    }
    
    public final void draw(Graphics g){
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
    
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeObject(playerTeam);
        oos.writeObject(aiTeam);
        oos.writeObject(currentMap);
        oos.writeObject(currentMinigame);
        //oos.writeUTF(remoteHostIp);
        //oos.writeObject(receiveWorldUpdate);
        //oos.writeObject(receiveControl);
        //oos.writeBoolean(isHosting);
        //oos.writeBoolean(isRemotelyHosted);
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        playerTeam = (Team)ois.readObject();
        aiTeam = (Team)ois.readObject();
        currentMap = (Map)ois.readObject();
        currentMinigame = (Battle)ois.readObject();
        
        //remoteHostIp = ois.readUTF(); //need to keep this, else readObject will read this
        //receiveWorldUpdate = (Consumer<ServerMessage>) ois.readObject();
        //receiveControl = (Consumer<ServerMessage>) ois.readObject();
        
        /*
        Was having a problem where serializing the world resulted in all users being hosts, as
        the serialized world was a host: BAD
        
        thus, receivers should set whether or not this is a host or remotely hosted themselves
        */
        //setIsHosting(false);
        //isRemotelyHosted = false;
        /*
        setIsHosting(ois.readBoolean());
        isRemotelyHosted = ois.readBoolean();
        if(isRemotelyHosted){
            setRemoteHost(remoteHostIp);
        }*/
        
        //particles = new SafeList<>();
        //createCanvas();
    }
}
