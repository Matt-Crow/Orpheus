package actions;

import entities.AbstractEntity;

public class OnUpdateEvent{
	private final AbstractEntity updatee;
    public OnUpdateEvent(AbstractEntity e){
		updatee = e;
	}
    public AbstractEntity getUpdated(){
        return updatee;
    }
}
