package actions;

import entities.Entity;

/**
 * Created whenever one Entity hits another.
 */
public class OnHitEvent {
    private final Entity hitter;
    private final Entity wasHit;
    
    /**
     * Creates the event.
     * @param hitter the Entity who collided with the other.
     * @param wasHit the Entity who was hit.
     */
    public OnHitEvent(Entity hitter, Entity wasHit){
        this.hitter = hitter;
        this.wasHit = wasHit;
    }
    
    /**
     * 
     * @return the Entity who struck another.
     */
    public Entity getHitter(){
        return hitter;
    }
    
    /**
     * 
     * @return the Entity who was hit.
     */
    public Entity getWasHit(){
        return wasHit;
    }
}
