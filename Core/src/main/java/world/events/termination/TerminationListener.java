package world.events.termination;

/**
 * denotes that a class should be notified when an object it is listening to
 * terminates
 */
@FunctionalInterface
public interface TerminationListener {

    /**
     * Fired whenever an object this is listening to terminates
     * @param terminable the object that just terminated
     */
    public void objectWasTerminated(Terminable terminable);
}
