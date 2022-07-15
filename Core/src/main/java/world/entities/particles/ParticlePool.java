package world.entities.particles;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * A ParticlePool associates Particles with how many frames they've been alive
 * for.
 * 
 * @author Matt Crow
 */
class ParticlePool {
    private int lifeSpan;
    private final LinkedList<Particle> particles;    
    
    ParticlePool(int lifeSpan){
        this.lifeSpan = lifeSpan;
        particles = new LinkedList<>();
    }
    
    public void add(Particle p){
        particles.add(p);
    }
    
    public void forEach(Consumer<Particle> doThis){
        particles.forEach(doThis);
    }
    
    public int getLifeSpan(){
        return lifeSpan;
    }
    
    public void updateLifeSpan(){
        --lifeSpan;
    }
    
    public boolean shouldTerminate(){
        return 0 == lifeSpan;
    }
}
