package world.entities.particles;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * A ParticleCollection handles storing, updating, and terminating Particles
 * This allows greater efficiency when managing a large number of Particles.
 * 
 * @author Matt Crow
 */
public class ParticleCollection {
    private final LinkedList<ParticlePool> pools;    
    
    public ParticleCollection(){
        pools = new LinkedList<>();
    }
    
    public void add(Particle p){
        int age = p.getLifeSpan();
        ParticlePool poolForAge = null;
        
        int i = pools.size() - 1;
        // no first part of for loop
        for( ; i >= 0 && poolForAge == null && pools.get(i).getLifeSpan() <= age; --i){
            if(pools.get(i).getLifeSpan() == age){
                poolForAge = pools.get(i);
            }
        }
        
        // no pool allocated yet
        if(poolForAge == null){
            poolForAge = new ParticlePool(age);
            pools.add(poolForAge);
        }
        
        poolForAge.add(p);
    }
    
    public void forEach(Consumer<Particle> doThis){
        pools.forEach((pool)->pool.forEach(doThis));
    }
    
    public void updatePoolAges(){
        pools.forEach((pool)->pool.updateLifeSpan());
        while(!pools.isEmpty() && pools.get(0).shouldTerminate()){
            pools.removeFirst();
        }
    }
    
    public void clear(){
        pools.clear();
    }
}
