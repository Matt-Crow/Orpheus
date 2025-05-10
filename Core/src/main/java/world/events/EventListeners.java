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
     * unregisters an event listener
     * @param listener the listener to unregister
     */
    public void remove(EventListener<T> listener) {
        listeners.remove(listener);
        next.remove(listener);
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

        // resolve bug where next wave of listeners not immediately added
        listeners.addAll(next); 
        next.clear();

        // this might be different from a filter, as handle could add stuff to next
        for (EventListener<T> listener : listeners) {
            listener.handle(e);
            if (isNotDone(listener)) {
                next.add(listener);
            }
        }

        listeners = next;
        next = new LinkedList<>();
    }

    private boolean isNotDone(EventListener<T> listener) {
        var isDone = listener.isDone();

        // unfortunately, some of my old code relies on this instanceof
        if (listener instanceof Terminable) {
            var asTerminable = (Terminable)listener;
            isDone = isDone || asTerminable.isTerminating();
        }
        
        return !isDone;
    }
}
