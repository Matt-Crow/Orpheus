package world;

import battle.Battle;
import battle.Team;
import controllers.Master;
import entities.AbstractEntity;
import entities.Particle;
import entities.AbstractPlayer;
import entities.HumanPlayer;
import entities.PlayerControls;
import entities.Projectile;
import graphics.CustomColors;
import graphics.Tile;
import graphics.Map;
import graphics.MapLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import windows.world.WorldCanvas;
import static java.lang.System.out;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import net.ServerMessage;
import net.ServerMessageType;
import util.Random;
import util.SafeList;
import util.SerialUtil;

/**
 * The AbstractWorld class acts as a controller for each game.
 * It keeps track of all Entities in the game by keeping track of each Team, 
 which in turn keeps track of various entities
 
 The world also keeps track of all Particles, as leaving them lumped in
 with Teams leads to drastic performance issues when serializing and checking
 for collisions.
 
 The AbstractWorld handles all the drawing and updating as well.
 * @author Matt Crow
 */
public abstract class AbstractWorld implements Serializable{
    private volatile Team playerTeam;
    private volatile Team AITeam;
    private transient SafeList<Particle> particles;
    private Map currentMap;
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    // I don't think I need to link this here.
    private transient WorldCanvas canvas; //transient means "don't serialize me!"
    
    /*
    Not sure whether I should encapsulate the 
    different behaviour in Strategies or subclasses,
    
    as Strategies require exposing some functions from this,
    but I wouldn't be able to cast serialized world from HostWorld to RemoteWorld...
    ... or would I?
    */
    // move these to strategies or subclasses
    private transient boolean isHosting; //whether or not this is the host of a game, and thus should manage itself for every player
    private transient boolean isRemotelyHosted; //whether or not another computer is running this AbstractWorld
    private String remoteHostIp;
    private Consumer<ServerMessage> receiveWorldUpdate;
    private Consumer<ServerMessage> receiveControl;
    
    public AbstractWorld(int size){
        playerTeam = new Team("Players", Color.green);
        AITeam = new Team("AI", Color.red);
        particles = new SafeList<>();
        
        currentMap = new Map(size, size);
        canvas = null;
        currentMinigame = null;
        
        isHosting = false;
        isRemotelyHosted = false;
        remoteHostIp = "";
        
        receiveWorldUpdate = (Consumer<ServerMessage> & Serializable)(sm)->receiveWorldUpdate(sm);
        receiveControl = (Consumer<ServerMessage> & Serializable) (sm)->receiveControl(sm);
    }
    
    /**
     * Creates the classic AbstractWorld where battles 
 take place: a 20x20 square.
     * 
     * Handles most of the initialization for you,
     * all you need to do is add teams,
     * then set it's minigame to a Battle, like so:
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
    public static AbstractWorld createDefaultBattle(){
        AbstractWorld w = new AbstractWorld(20);
        try {
            w.setMap(MapLoader.readCsv(AbstractWorld.class.getResourceAsStream("/testMap.csv")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        w.createCanvas();
        
        Tile block = new Tile(0, 0, CustomColors.GRAY);
        block.setBlocking(true);
        w.currentMap.addToTileSet(0, new Tile(0, 0, Color.BLUE));
        w.currentMap.addToTileSet(1, block);
        return w;
    }
    
    public final AbstractWorld setPlayerTeam(Team t){
        playerTeam = t;
        t.forEachMember((AbstractPlayer p)->p.setWorld(this));
        t.forEach((AbstractEntity e)->e.setWorld(this));
        return this;
    }
    
    public final AbstractWorld setEnemyTeam(Team t){
        AITeam = t;
        t.forEachMember((AbstractPlayer p)->p.setWorld(this));
        t.forEach((AbstractEntity e)->e.setWorld(this));
        return this;
    }
    
    public Team getPlayerTeam(){
        return playerTeam;
    }
    public Team getAITeam(){
        return AITeam;
    }
    
    public AbstractWorld addParticle(Particle p){
        particles.add(p);
        p.setWorld(this);
        return this;
    }
    
    public AbstractWorld setMap(Map m){
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
        if(c == null){
            throw new NullPointerException();
        }
        canvas = c;
    }
    public WorldCanvas getCanvas(){
        return canvas;
    }
    
    public void setCurrentMinigame(Battle b){
        if(b == null){
            throw new NullPointerException();
        }
        currentMinigame = b;
        b.setHost(this);
    }
    public Battle getCurrentMinigame(){
        return currentMinigame;
    }
    
    /**
     * Sets whether or not this AbstractWorld is
 hosting other players, and thus should
 send updates to its clients.
     * 
     * @param b whether or not this is a host for other players
     * @return this
     */
    public AbstractWorld setIsHosting(boolean b) throws IOException{
        if(b){
            if(!Master.SERVER.isStarted()){
                Master.SERVER.start();
            }
            Master.SERVER.addReceiver(ServerMessageType.CONTROL_PRESSED, receiveControl);
            isHosting = true;
        }
        return this;
    }
    
    public boolean isHosting(){
        return isHosting;
    }
    
    /**
     * Notifies this AbstractWorld that it is being generated
 by a different computer than this one. 
     * 
     * @param ipAddr the IP address to listen to 
 for changes to this AbstractWorld
     * @return this
     * @throws java.io.IOException is the server cannot start
     */
    public AbstractWorld setRemoteHost(String ipAddr) throws IOException{
        if(!Master.SERVER.isStarted()){
            Master.SERVER.start();
        }
        
        isRemotelyHosted = true;
        remoteHostIp = ipAddr;
        Master.SERVER.addReceiver(ServerMessageType.WORLD_UPDATE, receiveWorldUpdate);
        
        return this;
    }
    
    public boolean isRemotelyHosted(){
        return isRemotelyHosted;
    }
    
    public String getHostIp(){
        return remoteHostIp;
    }
    
    public void init(){
        particles.clear();
        if(currentMap != null){
            currentMap.init();
        }
        if(currentMinigame != null){
            currentMinigame.init();
        }
    }
    
    /**
     * Sets the coordinates of the given
 AbstractEntity onto a random valid tile
 on this' map.
     * 
     * @param e 
     */
    public void spawnIntoWorld(AbstractEntity e){
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
        e.setWorld(this);
    }
    
    private void hostUpdateTeam(Team t){
        t.update();
        t.forEach((AbstractEntity member)->{
            currentMap.checkForTileCollisions(member);
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
    private void hostUpdate(){
        hostUpdateTeam(playerTeam);
        hostUpdateTeam(AITeam);
        
        if(isHosting){
            Master.SERVER.send(new ServerMessage(
                SerialUtil.serializeToString(new Team[]{playerTeam, AITeam}),
                ServerMessageType.WORLD_UPDATE
            ));
        }
    }
        
    private void clientUpdate(){
        /*
        Client update is only called if isHosting is false
        so it runs for both clients and solo players.
        But, if this is also not remotely hosted, that makes
        it a solo player, thus, particles have already been spawned
        in hostUpdate, so no need to do this
        */
        if(isRemotelyHosted){
            playerTeam.forEach((AbstractEntity member)->{
                if(member instanceof Projectile){
                    ((Projectile)member).spawnParticles();
                }
            });
            AITeam.forEach((AbstractEntity member)->{
                if(member instanceof Projectile){
                    ((Projectile)member).spawnParticles();
                }
            });
        }
    }
    
    /**
     * Updates the world, performing functions based on whether or not the
 AbstractWorld is being hosted or is hosting:
 <table>
     *  <tr>
     *      <td>Table</td>
     *      <td>isHosting</td>
     *      <td>isn't hosting</td>
     *  </tr>
     *  <tr>
     *      <td>isHosted</td>
     *      <td>Shouldn't exist</td>
     *      <td>Only update particles and minigame</td>
     *  </tr>
     *  <tr>
     *      <td>isn't hosted</td>
     *      <td>update everything, send update message</td>
     *      <td>Is playing solo, so update everything</td>
     *  </tr>
     * </table>
     */
    public void update(){
        //synchronized(teams){
            if(!isRemotelyHosted){
                hostUpdate();
            }
            if(!isHosting){
                clientUpdate();
            }
            particles.forEach((p)->p.doUpdate()); //both host and client must update their particles
            if(currentMinigame != null){
                currentMinigame.update();
            }
        //}
    }
    
    private void receiveWorldUpdate(ServerMessage sm){
        //synchronized(teams){
        SwingUtilities.invokeLater(()->{
            Team[] ts = (Team[])SerialUtil.fromSerializedString(sm.getBody());
            setPlayerTeam(ts[0]);
            setEnemyTeam(ts[1]);

            Master.getUser().linkToRemotePlayerInWorld(this); //since teams have changed
        });
        
        //}
    }
    
    private void receiveControl(ServerMessage sm){
        HumanPlayer p = sm.getSender().getPlayer();
        PlayerControls.decode(p, sm.getBody());
    }
    
    public void draw(Graphics g){
        //synchronized(teams){
            currentMap.draw(g);
            particles.forEach((p)->p.draw(g));
            playerTeam.draw(g);
            AITeam.draw(g);
        //}
    }
    
    public void displayData(){
        out.println("WORLD:");
        out.println("Current minigame:");
        out.println((currentMinigame == null) ? "null" : "battle");
        //currentMinigame.displayData();
        out.println("Current map:");
        //currentMap.displayData();
        out.println("Teams:");
        playerTeam.displayData();
        AITeam.displayData();
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
        oos.writeObject(AITeam);
        oos.writeObject(currentMap);
        oos.writeObject(currentMinigame);
        oos.writeUTF(remoteHostIp);
        oos.writeObject(receiveWorldUpdate);
        oos.writeObject(receiveControl);
        //oos.writeBoolean(isHosting);
        //oos.writeBoolean(isRemotelyHosted);
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        playerTeam = (Team)ois.readObject();
        AITeam = (Team)ois.readObject();
        setMap((Map) ois.readObject());
        setCurrentMinigame((Battle) ois.readObject());
        
        remoteHostIp = ois.readUTF(); //need to keep this, else readObject will read this
        receiveWorldUpdate = (Consumer<ServerMessage>) ois.readObject();
        receiveControl = (Consumer<ServerMessage>) ois.readObject();
        
        /*
        Was having a problem where serializing the world resulted in all users being hosts, as
        the serialized world was a host: BAD
        
        thus, receivers should set whether or not this is a host or remotely hosted themselves
        */
        setIsHosting(false);
        isRemotelyHosted = false;
        /*
        setIsHosting(ois.readBoolean());
        isRemotelyHosted = ois.readBoolean();
        if(isRemotelyHosted){
            setRemoteHost(remoteHostIp);
        }*/
        
        particles = new SafeList<>();
        createCanvas();
    }
}
