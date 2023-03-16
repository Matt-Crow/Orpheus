package world.events;

import java.util.LinkedList;

import world.events.termination.Terminable;

/**
 * Maintains a list of instances of EventListener.
 * Composite design pattern
 */
public class EventListeners<T extends Event> implements EventListener<T> {
    
    /**
     * the event listeners which this will forward events to
     */
    private LinkedList<EventListener<T>> listeners = new LinkedList<>();

    /**
     * the event listeners that will be added to the next iteration - this 
     * avoids concurrent modification of listeners
     */
    private LinkedList<EventListener<T>> next = new LinkedList<>();

    /**
     * registers a new event listener
     * @param listener the listener to register
     */
    public void add(EventListener<T> listener) {
        next.add(listener);
    }

    /**
     * unregisters all event listeners
     */
    public void clear() {
        listeners.clear();
        next.clear();
    }

    @Override
    public void handle(T e) {
        for (EventListener<T> listener : listeners) {
            listener.handle(e);
            if ( // check if a terminable listener needs to be removed
                !(listener instanceof Terminable)
                || !((Terminable)listener).isTerminating()
            ) {
                next.add(listener);
            }
        }
        listeners = next;
        next = new LinkedList<>();
    }
}
