package actions;

import entities.Entity;

public class OnUpdateEvent{
	private final Entity updatee;
    public OnUpdateEvent(Entity e){
		updatee = e;
	}
    public Entity getUpdated(){
        return updatee;
    }
}
