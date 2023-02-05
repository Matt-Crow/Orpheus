package world.events.termination;

/**
 * denotes that a class can terminate
 */
public interface Terminable {
    
    /**
     * registers a new object to be notified when this terminates
     * @param listener the object to notify when this terminates
     */
    public void addTerminationListener(TerminationListener listener);

    /**
     * @return whether or not this object is in a termination state
     */
    public boolean isTerminating();

    /**
     * Notifies listeners that this has terminated.
     * It is an error to call this method multiple times.
     * Generally speaking, an instance of Terminables should call this method.
     * @see Terminables
     */
    public void terminate();
}
