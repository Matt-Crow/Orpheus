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
     * @param <T>
     * @param o
     */
    public <T extends Object & Terminable> void objectWasTerminated(Class<T> o);
}
