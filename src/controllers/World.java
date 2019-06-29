package controllers;

import battle.Battle;
import battle.Team;
import entities.Entity;
import entities.Player;
import entities.Projectile;
import graphics.Tile;
import graphics.Map;
import java.awt.Color;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import windows.WorldCanvas;
import static java.lang.System.out;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ServerMessage;
import net.ServerMessageType;
import util.SerialUtil;

/**
 * The World class will act as a controller for the game.
 * It keeps track of all Entities in the game by keeping track of each Team, 
 * which in turn keeps track of various entities
 * 
 * The World will handle all the drawing and updating as well.
 * @author Matt Crow
 */
public class World implements Serializable{
    //key is team ID
    private HashMap<Integer, Team> teams; //makes it faster to find nearest enemies
    
    private Map currentMap;
    private transient WorldCanvas canvas; //transient means "don't serialize me!"
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    private transient boolean isHosting; //whether or not this is the host of a game, and thus should manage itself for every player
    private transient boolean isRemotelyHosted; //whether or not another computer is running this World
    
    private transient final Consumer<ServerMessage> receiveWorldUpdate;
    
    public World(int size){
        teams = new HashMap<>();
        
        currentMap = new Map(size, size);
        canvas = null;
        currentMinigame = null;
        
        isHosting = false;
        isRemotelyHosted = false;
        
        receiveWorldUpdate = (sm)->receiveWorldUpdate(sm);
    }
    
    /**
     * Creates the classic World where battles 
     * take place: a 20x20 square.
     * 
     * Handles most of the initialization for you,
     * all you need to do is add teams,
     * then set it's minigame to a Battle, like so:
     * <pre>{@code 
     *  World newWorld = World.createDefaultBattle();
     *  newWorld.addTeam(team1);
     *  newWorld.addTeam(team2);
     *  Battle battle = new Battle(
     *      newWorld.getCanvas(),
     *      team1,
     *      team2
     *  );
     *  newWorld.setCurrentMinigame(battle);
     *  battle.setHost(newWorld);
     *  newWorld.init();
     *  //can change this to switchToPage once world canvas is a Page
     *  JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
     *  parent.setContentPane(newWorld.getCanvas());
     *  parent.revalidate();
     *  newWorld.getCanvas().requestFocus();
     * }</pre>
     * it's that simple!
     * @return the newly created world.
     */
    public static World createDefaultBattle(){
        World w = new World(20);
        w.createCanvas();
        w.currentMap.addToTileSet(0, new Tile(0, 0, Color.BLUE));
        return w;
    }
    
    public final World addTeam(Team t){
        teams.put(t.getId(), t);
        t.forEachMember((Player p)->p.setWorld(this));
        return this;
    }
    
    /**
     * Returns the team with the given ID,
     * if one exists. This is used for serialization
     * so that users know which team they are on 
     * 
     * @param id the ID of the team to search for
     * @return the team in this World with the given ID, or null if one doesn't exist
     */
    public Team getTeamById(int id){
        return teams.get(id);
    }
    
    public Team[] getTeams(){
        return teams.values().toArray(new Team[teams.size()]);
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
        b.setHost(this);
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
    
    /**
     * Sets whether or not this World is
     * hosting other players, and thus should
     * send updates to its clients.
     * 
     * @param b whether or not this is a host for other players
     * @return this
     */
    public World setIsHosting(boolean b){
        if(b){
            if(Master.getServer() == null){
                try {
                    Master.startServer();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(Master.getServer() != null){
                isHosting = b;
                Master.getServer().addReceiver(ServerMessageType.CONTROL_PRESSED, (sm)->{
                    out.println("World received this in setIsHosting:");
                    sm.displayData();
                });
            }
        }
        return this;
    }
    
    public boolean isHosting(){
        return isHosting;
    }
    
    /**
     * Notifies this World that it is being generated
     * by a different computer than this one. 
     * 
     * @param ipAddr the IP address to listen to 
     * for changes to this World
     * @return this
     */
    public World setRemoteHost(String ipAddr){
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(Master.getServer() != null){
            //successfully started
            isRemotelyHosted = true;
            Master.getServer().addReceiver(ServerMessageType.WORLD_UPDATE, (sm)->{
                out.println("World received this in setRemoteHost:");
                sm.displayData();
            });
        }
        return this;
    }
    
    public boolean isRemotelyHosted(){
        return isRemotelyHosted;
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
        if(isRemotelyHosted){
            /*
            only the host should
            update the world
            */
            return;
        }
        if(currentMinigame != null){
            currentMinigame.update();
        }
        teams.values().stream().forEach((Team t)->{
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
        if(isHosting){
            Master.getServer().send(new ServerMessage(
                SerialUtil.serializeToString(teams),
                ServerMessageType.WORLD_UPDATE
            ));
        }
    }
    
    private void receiveWorldUpdate(ServerMessage sm){
        HashMap<Integer, Team> ts = (HashMap<Integer, Team>)SerialUtil.fromSerializedString(sm.getBody());
        ts.forEach((i, t)->{
            out.println(i + ": ");
            t.displayData();
        });
        
        teams = ts;
    }
    
    public void draw(Graphics g){
        currentMap.draw(g);
        teams.values().stream().forEach((t)->t.draw(g));
    }
    
    public void displayData(){
        out.println("WORLD:");
        out.println("Current minigame:");
        out.println((currentMinigame == null) ? "null" : "battle");
        //currentMinigame.displayData();
        out.println("Current map:");
        //currentMap.displayData();
        out.println("Teams:");
        teams.values().stream().forEach((Team t)->{
            t.displayData();
        });
    }
    
    /**
     * Serializes this using ObjectOutputStream.writeObject,
     * then converts the result to a string so that it can
     * be used as the body of a ServerMessage
     * 
     * This has a problem where if there is too much data
     * in the world, such as in a 99 vs 99, it cannot be encoded
     * 
     * @return the serialized version of this World,
     * converted to a string for convenience.
     * 
     * @see SerialUtil#serializeToString
     */
    public String serializeToString(){
        return SerialUtil.serializeToString(this);
    }
    
    /**
     * Converts a String generated from World.serializeToString,
     * and de-serializes it into a copy of that original world.
     * It is important to note that the World's canvas is not serialized,
     * so be sure to call either setCanvas, or createCanvas so that the World
     * can be rendered
     * 
     * @param s a string variant of an object stream serialization of a World
     * @return a copy of the world which was serialized into a string
     * 
     * @see SerialUtil#fromSerializedString(java.lang.String) 
     */
    public static World fromSerializedString(String s){
        return (World)SerialUtil.fromSerializedString(s);
    }
}
