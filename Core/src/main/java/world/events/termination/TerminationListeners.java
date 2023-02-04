package world.events.termination;

import java.util.LinkedList;

/**
 * Maintains and manages a list of TerminationListeners.
 * Composite design pattern to reduce code duplication.
 */
public class TerminationListeners implements TerminationListener {
    /**
     * the collection of objects this is managing
     */
    private final LinkedList<TerminationListener> listeners = new LinkedList<>();

    /**
     * registers a new termination listener for this to keep track of
     * @param listener the listener to track
     */
    public void add(TerminationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void objectWasTerminated(Terminable terminable) {
        listeners.forEach((listener) -> listener.objectWasTerminated(terminable));
        listeners.clear();
    }
}
