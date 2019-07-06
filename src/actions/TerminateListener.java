package actions;

/**
 * Used to listen for terminate() being
 * invoked on an Object implementing Terminable.
 * 
 * @author Matt Crow
 */
public interface TerminateListener {
    /**
     * Invoked upon the given object
     * terminating.
     * 
     * @param o
     */
    public void objectWasTerminated(Object o);
}
