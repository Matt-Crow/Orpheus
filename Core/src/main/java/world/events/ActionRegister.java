package world.events;

import orpheus.core.world.occupants.WorldOccupant;
import orpheus.core.world.occupants.players.Player;
/**
 * An ActionRegister is used to store OnHit- and OnUpdate-Listeners, and register them to WorldOccupant.
 * It is used by the AbstractPlayer class to store Status effects and passives.
 */
public class ActionRegister {
	private final WorldOccupant registeredTo;
	private final EventListeners<OnHitEvent> onHitListeners = new EventListeners<>();
	private final EventListeners<OnUpdateEvent> onUpdateListeners = new EventListeners<>();
	
    /**
     * Stores Listeners for an AbstractEntity.
     * @param e the AbstractEntity to store Listeners for.
     */
	public ActionRegister(WorldOccupant e){
		registeredTo = e;
	}

	/**
     * adds a Listener that will fire whenever this AbstractEntity lands an attack on another.
     * @param a the listener to add.
     */
	public void addOnHit(EventListener<OnHitEvent> listener) {
		onHitListeners.add(listener);
	}

	public void addOnUpdate(EventListener<OnUpdateEvent> listener) {
		onUpdateListeners.add(listener);
	}

	public void triggerOnHit(Player hit){
		OnHitEvent t = new OnHitEvent(registeredTo, hit);
		onHitListeners.handle(t);
	}

	public void triggerOnUpdate(){
        OnUpdateEvent e = new OnUpdateEvent(registeredTo);
		onUpdateListeners.handle(e);
	}

	public void reset(){
		onHitListeners.clear();
		onUpdateListeners.clear();
	}
}
