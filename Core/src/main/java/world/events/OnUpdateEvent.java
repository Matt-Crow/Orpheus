package world.events;

import world.entities.AbstractEntity;

public class OnUpdateEvent implements Event {
	private final AbstractEntity updatee;

    public OnUpdateEvent(AbstractEntity e){
		updatee = e;
	}
    
    public AbstractEntity getUpdated(){
        return updatee;
    }
}
