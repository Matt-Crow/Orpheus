package world.events;

import orpheus.core.world.occupants.WorldOccupant;

public class OnUpdateEvent implements Event {
	private final WorldOccupant updatee;

    public OnUpdateEvent(WorldOccupant e){
		updatee = e;
	}
    
    public WorldOccupant getUpdated(){
        return updatee;
    }
}
