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
import util.SerialUtil;

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
    
    /**
     * Creates the classic AbstractWorld where battles 
     * take place: a 20x20 square.
     * 
     * Handles most of the initialization for you,
     * all you need to do is add teams,
     * then set it's minigame to a Battle, like so:
     * 
     * UPDATE THIS
     * 
     * <pre>{@code 
  AbstractWorld newWorld = AbstractWorld.createDefaultBattle();
  newWorld.addTeam(team1);
  newWorld.addTeam(team2);
  Battle battle = new Battle();
  newWorld.setCurrentMinigame(battle);
  battle.setHost(newWorld);
  newWorld.init();
  //can change this to switchToPage once world canvas is a Page
  JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
  parent.setContentPane(newWorld.getCanvas());
  parent.revalidate();
  newWorld.getCanvas().requestFocus();
 }</pre>
     * it's that simple!
     * @return the newly created world.
     */
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
    
    /**
     * Serializes this using ObjectOutputStream.writeObject,
     * then converts the result to a string so that it can
     * be used as the body of a ServerMessage
     * 
     * This has a problem where if there is too much data
     * in the world, such as in a 99 vs 99, it cannot be encoded
     * 
     * @return the serialized version of this AbstractWorld,
 converted to a string for convenience.
     * 
     * @see SerialUtil#serializeToString
     */
    public String serializeToString(){
        return SerialUtil.serializeToString(this);
    }
    
    /**
     * Converts a String generated from AbstractWorld.serializeToString,
 and de-serializes it into a copy of that original world.
     * It is important to note that the AbstractWorld's canvas is not serialized,
 so be sure to call either setCanvas, or createCanvas so that the AbstractWorld
 can be rendered
     * 
     * @param s a string variant of an object stream serialization of a AbstractWorld
     * @return a copy of the world which was serialized into a string
     * 
     * @see SerialUtil#fromSerializedString(java.lang.String) 
     */
    public static AbstractWorld fromSerializedString(String s){
        return (AbstractWorld)SerialUtil.fromSerializedString(s);
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
