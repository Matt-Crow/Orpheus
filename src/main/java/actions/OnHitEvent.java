package actions;

import entities.AbstractEntity;

/**
 * Created whenever one AbstractEntity hits another.
 */
public class OnHitEvent {
    private final AbstractEntity hitter;
    private final AbstractEntity wasHit;
    
    /**
     * Creates the event.
     * @param hitter the AbstractEntity who collided with the other.
     * @param wasHit the AbstractEntity who was hit.
     */
    public OnHitEvent(AbstractEntity hitter, AbstractEntity wasHit){
        this.hitter = hitter;
        this.wasHit = wasHit;
    }
    
    /**
     * 
     * @return the AbstractEntity who struck another.
     */
    public AbstractEntity getHitter(){
        return hitter;
    }
    
    /**
     * 
     * @return the AbstractEntity who was hit.
     */
    public AbstractEntity getWasHit(){
        return wasHit;
    }
}
