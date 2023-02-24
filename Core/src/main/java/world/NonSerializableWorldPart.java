package world;

import java.util.Collection;

import world.entities.particles.Particle;
import world.entities.particles.ParticleCollection;

/**
 * This is the part of the world that is not serialized, as it either does not
 * change, or is purely cosmetic, and thus has no impact on gameplay, and would
 * slow down serialization too much if serialized.
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

    protected Collection<Particle> getParticles() {
        return particles.toList();
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
}
