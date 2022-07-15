package world;

import java.awt.Graphics;
import world.entities.particles.Particle;
import world.entities.particles.ParticleCollection;

/**
 * This is the part of the world that is not serialized.
 * 
 * Keeps track of all Particles, as leaving them lumped in with Teams leads to 
 * drastic performance issues when serializing.
 * 
 * Essentially, provides a stable, non-serialized parallel to the volatile, 
 * serialized part.
 * 
 * @author Matt Crow
 */
public class NonSerializableWorldPart {
    private final ParticleCollection particles;
    
    protected NonSerializableWorldPart(){
        particles = new ParticleCollection();
    }
    
    protected void addParticle(Particle p){
        particles.add(p);
    }
    
    protected void init(){
        particles.clear();
    }
    
    /**
     * Updates all the non-serialized
     * contents of this world. Note this
     * does not affect the host in any way,
     * just this client.
     */
    protected void update(){
        particles.forEach((p)->p.update());
        particles.updatePoolAges();
    }
    
    protected void draw(Graphics g){
        particles.forEach((p)->p.draw(g));
    }
}
