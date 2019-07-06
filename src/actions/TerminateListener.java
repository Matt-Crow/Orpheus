package actions;

/**
 * Used to listen for terminate() being
 * invoked on an Object implementing Terminable.
 * To use this interface, simply override the method,
 * and call (Object implementing Terminable).addTerminationListener((Object implementing TerminateListener));
 * just don't forget to call removeTerminateListener in objectWasTerminated, as this saves memory
 * 
 * @see Terminable
 * @author Matt Crow
 */
public interface TerminateListener {
    /**
     * Invoked upon the given object
     * terminating.
     * 
     * @param o the object which was terminated
     */
    public void objectWasTerminated(Object o);
}
