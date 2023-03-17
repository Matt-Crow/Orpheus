package world.events;

import orpheus.core.world.occupants.WorldOccupant;

/**
 * Created whenever one AbstractEntity hits another.
 */
public class OnHitEvent implements Event {
    private final WorldOccupant hitter;
    private final WorldOccupant wasHit;
    
    /**
     * Creates the event.
     * @param hitter the AbstractEntity who collided with the other.
     * @param wasHit the AbstractEntity who was hit.
     */
    public OnHitEvent(WorldOccupant hitter, WorldOccupant wasHit){
        this.hitter = hitter;
        this.wasHit = wasHit;
    }
    
    /**
     * 
     * @return the AbstractEntity who struck another.
     */
    public WorldOccupant getHitter(){
        return hitter;
    }
    
    /**
     * 
     * @return the AbstractEntity who was hit.
     */
    public WorldOccupant getWasHit(){
        return wasHit;
    }
}
