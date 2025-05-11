package world.events;

/**
 * Denotes that a class listens for events in the application
 */
@FunctionalInterface
public interface EventListener<T> {
    
    /**
     * called whenever a subject this is registered with emits an event
     * @param e the event emitted by a subject this is listening to
     */
    public void handle(T e);

    /**
     * @return whether this event listener is done, and thus should be removed
     *  from the collection of event listeners
     */
    public default boolean isDone() {
        return false;
    }
}
