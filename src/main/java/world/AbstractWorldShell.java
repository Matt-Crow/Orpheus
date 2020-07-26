package world;

import battle.Battle;
import battle.Team;
import entities.AbstractEntity;
import entities.Particle;
import entities.AbstractPlayer;
import graphics.Map;
import java.awt.Graphics;
import windows.world.WorldCanvas;
import static java.lang.System.out;
import util.SafeList;

/**
 * The AbstractWorldShell class acts as a shell
 * for WorldContent. Essentially, it allows SoloWorlds,
 * HostWorlds, and RemoteProxyWorlds to have different
 * behaviors for interacting with the underlying WorldContent.
 * The world also keeps track of all Particles, as leaving them lumped in
 * with Teams leads to drastic performance issues when serializing and checking
 * for collisions.
 * 
 * Essentially, the AbstractWorldShell provides
 * a stable, non-serialized interface with 
 * the volatile, serialized WorldContent.
 * 
 * The AbstractWorldShell handles all the drawing and updating as well.
 * @author Matt Crow
 */
public abstract class AbstractWorldShell {
    private final SafeList<Particle> particles;
    
    // I don't think I need to link this here.
    private WorldCanvas canvas;
        
    private volatile WorldContent content;
    
    public AbstractWorldShell(WorldContent worldContent){
        particles = new SafeList<>();
        canvas = null;
        content = worldContent;
        worldContent.setShell(this);
    }
    
    public final AbstractWorldShell setPlayerTeam(Team t){
        content.setPlayerTeam(t);
        return this;
    }
    public final AbstractWorldShell setEnemyTeam(Team t){
        content.setAITeam(t);
        return this;
    }
    
    public Team getPlayerTeam(){
        return content.getPlayerTeam();
    }
    public Team getAITeam(){
        return content.getAITeam();
    }
    
    public AbstractWorldShell addParticle(Particle p){
        particles.add(p);
        p.setWorld(content); // do I need this?
        return this;
    }
    
    public AbstractWorldShell setMap(Map m){
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
        b.setHost(content);
    }
    public Battle getCurrentMinigame(){
        return content.getMinigame();
    }  
    
    public final void setContent(WorldContent newContent){
        content = newContent; 
        content.setShell(this);
        //content.getPlayerTeam().forEach((e)->e.setWorld(this));
        //content.getAITeam().forEach((e)->e.setWorld(this));
        Battle game = content.getMinigame();
        if(game != null){
            //game.setHost(this);
        }
        
    }
    public final WorldContent getContent(){
        return content;
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
        e.setWorld(content);
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
}
