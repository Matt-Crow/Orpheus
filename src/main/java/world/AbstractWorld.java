package world;

import battle.Battle;
import battle.Team;
import entities.AbstractEntity;
import entities.Particle;
import entities.AbstractPlayer;
import graphics.Map;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import windows.world.WorldCanvas;
import static java.lang.System.out;
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
    // keep this here for now
    private transient SafeList<Particle> particles;
    
    // I don't think I need to link this here.
    private transient WorldCanvas canvas; //transient means "don't serialize me!"
        
    private volatile WorldContent content;
    
    public AbstractWorld(WorldContent worldContent){
        particles = new SafeList<>();
        canvas = null;
        content = worldContent;
    }
    
    public final AbstractWorld setPlayerTeam(Team t){
        content.setPlayerTeam(t);
        t.forEachMember((AbstractPlayer p)->p.setWorld(this));
        t.forEach((AbstractEntity e)->e.setWorld(this));
        return this;
    }
    public final AbstractWorld setEnemyTeam(Team t){
        content.setAITeam(t);
        t.forEachMember((AbstractPlayer p)->p.setWorld(this));
        t.forEach((AbstractEntity e)->e.setWorld(this));
        return this;
    }
    
    public Team getPlayerTeam(){
        return content.getPlayerTeam();
    }
    public Team getAITeam(){
        return content.getAITeam();
    }
    
    public AbstractWorld addParticle(Particle p){
        particles.add(p);
        p.setWorld(this);
        return this;
    }
    
    public AbstractWorld setMap(Map m){
        content.setMap(m);
        return this;
    }
    public Map getMap(){
        return content.getMap();
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
        content.setMinigame(b);
        b.setHost(this);
    }
    public Battle getCurrentMinigame(){
        return content.getMinigame();
    }  
    
    public void init(){
        content.init();
        particles.clear();
    }
    
    /**
     * Sets the coordinates of the given
     * AbstractEntity onto a random valid tile
     * on this' map.
     * 
     * @param e 
     */
    public void spawnIntoWorld(AbstractEntity e){
        content.spawnIntoWorld(e);
        e.setWorld(this);
    }
    
    public final void updateParticles(){
        particles.forEach((p)->p.doUpdate());
    }
    public final void updateMinigame(){
        content.update();
    }
    
    
    
    
    
    public void draw(Graphics g){
        content.draw(g);
        particles.forEach((p)->p.draw(g));
    }
    
    public void displayData(){
        out.println("WORLD:");
        out.println("Current minigame:");
        out.println((getCurrentMinigame() == null) ? "null" : "battle");
        //currentMinigame.displayData();
        out.println("Current map:");
        //currentMap.displayData();
        out.println("Teams:");
        getPlayerTeam().displayData();
        getAITeam().displayData();
    }
    
    
    
    public abstract void update();
    
    
    
    
    
    
    
    /*
    Need to redo all of this
    */
    
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
        //oos.writeObject(playerTeam);
        //oos.writeObject(AITeam);
        //oos.writeObject(currentMap);
        //oos.writeObject(currentMinigame);
        //oos.writeUTF(remoteHostIp);
        //oos.writeObject(receiveWorldUpdate);
        //oos.writeObject(receiveControl);
        //oos.writeBoolean(isHosting);
        //oos.writeBoolean(isRemotelyHosted);
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
        //playerTeam = (Team)ois.readObject();
        //AITeam = (Team)ois.readObject();
        //setMap((Map) ois.readObject());
        //setCurrentMinigame((Battle) ois.readObject());
        
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
        
        particles = new SafeList<>();
        createCanvas();
    }
}
