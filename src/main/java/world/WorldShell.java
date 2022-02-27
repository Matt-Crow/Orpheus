package world;

import world.entities.particles.Particle;
import java.awt.Graphics;
import world.entities.particles.ParticleCollection;

/**
 * Keeps track of all Particles, as leaving them lumped in
 * with Teams leads to drastic performance issues when serializing and checking
 * for collisions.
 * 
 * Essentially, the WorldShell provides
 a stable, non-serialized interface with 
 the volatile, serialized WorldContent.
 * 
 * @author Matt Crow
 */
public class WorldShell {
    private final ParticleCollection particles;
    
    private TempWorld temp; // remove once Entities reference TempWorld
    
    public WorldShell(){
        particles = new ParticleCollection();
    }
    
    public void setTempWorld(TempWorld temp){
        this.temp = temp;
    }
    public TempWorld getTempWorld(){
        return temp;
    }
    
    public void addParticle(Particle p){
        particles.add(p);
    }
    
    public void init(){
        particles.clear();
    }
    
    public void draw(Graphics g){
        particles.forEach((p)->p.draw(g));
    }
    
    /**
     * Updates all the non-serialized
     * contents of this world. Note this
     * does not affect the host in any way,
     * just this client.
     */
    public void update(){
        updateParticles();
    };
    
    public final void updateParticles(){
        particles.forEach((p)->p.update());
        particles.updatePoolAges();
    }
}
