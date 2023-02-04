package world.events.termination;

import java.util.LinkedList;

/**
 * Maintains and manages a collection of Terminable objects
 */
public class Terminables {
    /**
     * the collection of objects this is managing
     */
    private LinkedList<Terminable> terminables = new LinkedList<>();

    /**
     * registers a new terminable for this to keep track of
     * @param t the terminable for this to track
     */
    public void add(Terminable t) {
        terminables.add(t);
    }

    /**
     * terminates and removes any objects that are in a termination state
     */
    public void update() {
        LinkedList<Terminable> notTerminated = new LinkedList<>();
        for(Terminable t : terminables) {
            if (t.isTerminating()) {
                t.terminate();
            } else {
                notTerminated.add(t);
            }
        }
        terminables = notTerminated;
    }
}
