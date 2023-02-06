package world.events;

import java.io.Serializable;

/**
 * Denotes that a class listens for events in the application
 */
@FunctionalInterface
public interface EventListener<T extends Event> extends Serializable {
    
    /**
     * called whenever a subject this is registered with emits an event
     * @param e the event emitted by a subject this is listening to
     */
    public void handle(T e);
}
