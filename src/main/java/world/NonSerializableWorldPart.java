package world;

import java.awt.Graphics;
import world.entities.particles.Particle;
import world.entities.particles.ParticleCollection;

/**
 * This is the part of the world that is not serialized.
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
    
    protected void update(){
        particles.forEach((p)->p.update());
        particles.updatePoolAges();
    }
    
    protected void draw(Graphics g){
        particles.forEach((p)->p.draw(g));
    }
}
