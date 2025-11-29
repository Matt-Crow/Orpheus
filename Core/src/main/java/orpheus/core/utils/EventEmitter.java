package orpheus.core.utils;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Emits events of type T, notifying listeners
 */
public class EventEmitter<T> {
    private final LinkedList<Consumer<T>> eventListeners = new LinkedList<>();

    public void addEventListener(Consumer<T> eventListener) {
        eventListeners.add(eventListener);
    }

    public void emitEvent(T event) {
        eventListeners.forEach(el -> el.accept(event));
    }
}
