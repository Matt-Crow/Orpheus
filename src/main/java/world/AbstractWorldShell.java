package world;

import world.battle.Battle;
import world.battle.Team;
import world.entities.particles.Particle;
import gui.graphics.Map;
import java.awt.Graphics;
import gui.pages.worldPlay.WorldCanvas;
import world.entities.particles.ParticleCollection;

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
    private final ParticleCollection particles;
    
    // I don't think I need to link this here.
    private WorldCanvas canvas;
        
    private volatile WorldContent content;
    
    public AbstractWorldShell(WorldContent worldContent){
        particles = new ParticleCollection();
        canvas = null;
        content = worldContent;
        worldContent.setShell(this);
    }
    
    public Team getPlayerTeam(){
        return content.getPlayers();
    }
    
    public Team getAITeam(){
        return content.getAITeam();
    }
    
    public AbstractWorldShell addParticle(Particle p){
        particles.add(p);
        return this;
    }
    
    public Map getMap(){
        return content.getMap();
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
    }
    
    public final WorldContent getContent(){
        return content;
    }
    
    public void init(){
        content.init();
        particles.clear();
    }
    
    public final void updateParticles(){
        particles.forEach((p)->p.update());
        particles.updatePoolAges();
    }
    public final void updateMinigame(){
        content.update();
    }
    
    public void draw(Graphics g){
        content.draw(g);
        particles.forEach((p)->p.draw(g));
    }
    
    public abstract void update();
}
