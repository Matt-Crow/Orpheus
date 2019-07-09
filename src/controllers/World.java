package controllers;

import battle.Battle;
import battle.Team;
import entities.Entity;
import entities.Particle;
import entities.Player;
import entities.PlayerControls;
import entities.Projectile;
import graphics.Tile;
import graphics.Map;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import windows.world.WorldCanvas;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.function.Consumer;
import net.ServerMessage;
import net.ServerMessageType;
import util.SafeList;
import util.SerialUtil;

/**
 * The World class acts as a controller for each game.
 * It keeps track of all Entities in the game by keeping track of each Team, 
 * which in turn keeps track of various entities
 * 
 * The world also keeps track of all Particles, as leaving them lumped in
 * with Teams leads to drastic performance issues when serializing and checking
 * for collisions.
 * 
 * The World handles all the drawing and updating as well.
 * @author Matt Crow
 */
public class World implements Serializable{
    //key is team ID
    private volatile HashMap<Integer, Team> teams; //makes it faster to find nearest enemies
    
    private transient SafeList<Particle> particles;
    
    private Map currentMap;
    private transient WorldCanvas canvas; //transient means "don't serialize me!"
    private Battle currentMinigame; //in future versions, this will be changed to include other minigames
    
    private transient boolean isHosting; //whether or not this is the host of a game, and thus should manage itself for every player
    private transient boolean isRemotelyHosted; //whether or not another computer is running this World
    private String remoteHostIp;
    
    private Consumer<ServerMessage> receiveWorldUpdate;
    private Consumer<ServerMessage> receiveControl;
    
    public World(int size){
        teams = new HashMap<>();
        
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
     *  Battle battle = new Battle();
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
        //t.forEachMember((Player p)->p.setWorld(this));
        t.forEach((Entity e)->e.setWorld(this));
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
    
    
    public World addParticle(Particle p){
        particles.add(p);
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
                Master.getServer().addReceiver(ServerMessageType.CONTROL_PRESSED, receiveControl);
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
            remoteHostIp = ipAddr;
            Master.getServer().addReceiver(ServerMessageType.WORLD_UPDATE, receiveWorldUpdate);
        }
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
    
    
    private void hostUpdate(){
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
    private void clientUpdate(){
        
        /*
        Client update is only called if isHosting is false
        so it runs for both clients and solo players.
        But, if this is also not remotely hosted, that makes
        it a solo player, thus, particles have already been spawned
        in hostUpdate, so no need to do this
        */
        System.out.println(this + " is updating");
        if(isRemotelyHosted){
            teams.values().stream().forEach((Team t)->{
                t.forEach((Entity member)->{
                    if(member instanceof Projectile){
                        System.out.println(member + " spawned a particle into " + member.getWorld());
                        ((Projectile)member).spawnParticles();
                    }
                });
            });
            //out.println("Now I have " + this.particles.length() + " particles");
        }
    }
    
    /**
     * Updates the world, performing functions based on whether or not the
     * World is being hosted or is hosting:
     * <table>
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
        synchronized(this){
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
        }
    }
    
    private void receiveWorldUpdate(ServerMessage sm){
        synchronized(this){
            teams.clear();

            HashMap<Integer, Team> ts = (HashMap<Integer, Team>)SerialUtil.fromSerializedString(sm.getBody());
            ts.values().stream().forEach((Team t)->addTeam(t));

            Master.getUser().linkToRemotePlayerInWorld(this); //since teams have changed
            if(canvas != null){
                canvas.repaint();
            }
        }
    }
    
    private void receiveControl(ServerMessage sm){
        synchronized(this){
            Player p = sm.getSender().getPlayer();
            System.out.println("Received control from " + p);
            PlayerControls.decode(p, sm.getBody());
        }
    }
    
    public void draw(Graphics g){
        synchronized(this){
            currentMap.draw(g);
            teams.values().stream().forEach((t)->{
                t.draw(g);
            });
            particles.forEach((p)->p.draw(g));
        }
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

    
    private void writeObject(ObjectOutputStream oos) throws IOException{
        oos.writeObject(teams);
        oos.writeObject(currentMap);
        oos.writeObject(currentMinigame);
        oos.writeUTF(remoteHostIp);
        oos.writeObject(receiveWorldUpdate);
        oos.writeObject(receiveControl);
        //oos.writeBoolean(isHosting);
        //oos.writeBoolean(isRemotelyHosted);
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        teams = (HashMap<Integer, Team>) ois.readObject();
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
