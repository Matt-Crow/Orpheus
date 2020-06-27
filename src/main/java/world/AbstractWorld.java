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
 * The AbstractWorld class acts as a controller for each game.
 * It keeps track of all Entities in the game by keeping track of each Team, 
 which in turn keeps track of various entities
 
 The world also keeps track of all Particles, as leaving them lumped in
 with Teams leads to drastic performance issues when serializing and checking
 for collisions.
 
 The AbstractWorld handles all the drawing and updating as well.
 * @author Matt Crow
 */
public abstract class AbstractWorld {
    // keep this here for now
    private final SafeList<Particle> particles;
    
    // I don't think I need to link this here.
    private WorldCanvas canvas;
        
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
    
    public final void setContent(WorldContent newContent){
        content = newContent; 
        content.getPlayerTeam().forEach((e)->e.setWorld(this));
        content.getAITeam().forEach((e)->e.setWorld(this));
        Battle game = content.getMinigame();
        if(game != null){
            game.setHost(this);
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
}
