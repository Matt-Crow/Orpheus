package world.events;

import world.entities.AbstractPlayer;
import world.entities.AbstractEntity;
import java.io.Serializable;
/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to an AbstractEntity.
 * It is used by the AbstractPlayer class to store Status effects and passives.
 */
public class ActionRegister implements Serializable {
	private final AbstractEntity registeredTo;
	private final EventListeners<OnHitEvent> onHitListeners = new EventListeners<>();
	private final EventListeners<OnHitEvent> onBeHitListeners = new EventListeners<>();
	private final EventListeners<OnUpdateEvent> onUpdateListeners = new EventListeners<>();
	
    /**
     * Stores Listeners for an AbstractEntity.
     * @param e the AbstractEntity to store Listeners for.
     */
	public ActionRegister(AbstractEntity e){
		registeredTo = e;
	}

	/**
     * adds a Listener that will fire whenever this AbstractEntity lands an attack on another.
     * @param a the listener to add.
     */
	public void addOnHit(EventListener<OnHitEvent> listener) {
		onHitListeners.add(listener);
	}
    
    /**
     * Adds a Listener that will fire whenever this AbstractEntity is struck by another.
     * @param a the listener to add.
     */
	public void addOnBeHit(EventListener<OnHitEvent> a){
		onBeHitListeners.add(a);
	}

	public void addOnUpdate(EventListener<OnUpdateEvent> listener) {
		onUpdateListeners.add(listener);
	}

	public void triggerOnHit(AbstractPlayer hit){
		OnHitEvent t = new OnHitEvent(registeredTo, hit);
		onHitListeners.handle(t);
	}

	public void triggerOnHitReceived(AbstractPlayer hitBy){
		OnHitEvent t = new OnHitEvent(hitBy, registeredTo);
		onBeHitListeners.handle(t);
    }

	public void triggerOnUpdate(){
        OnUpdateEvent e = new OnUpdateEvent(registeredTo);
		onUpdateListeners.handle(e);
	}
	public void reset(){
		onHitListeners.clear();
		onBeHitListeners.clear();
		onUpdateListeners.clear();
	}
}
