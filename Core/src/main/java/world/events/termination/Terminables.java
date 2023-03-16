package world.events.termination;

import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * Maintains and manages a collection of Terminable objects
 */
public class Terminables<T extends Terminable> {
    /**
     * the collection of objects this is managing
     */
    private LinkedList<T> terminables = new LinkedList<>();

    /**
     * the terminables that will be added in the next call to update
     */
    private LinkedList<T> next = new LinkedList<>();

    /**
     * registers a new terminable for this to keep track of
     * @param t the terminable for this to track
     */
    public void add(T t) {
        next.add(t);
    }

    /**
     * calls the given consumer on each terminable
     * @param doThis called on each element in this
     */
    public void forEach(Consumer<T> doThis) {
        terminables.forEach(doThis);
    }

    /**
     * removes all terminables from this
     */
    public void clear() {
        terminables.clear();
        next.clear();
    }

    /**
     * terminates and removes any objects that are in a termination state
     */
    public void update() {
        for(T t : terminables) {
            if (t.isTerminating()) {
                t.terminate();
            } else {
                next.add(t);
            }
        }
        terminables = next;
        next = new LinkedList<>();
    }
}
