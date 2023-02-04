package world.events;

import world.events.termination.TerminationListener;

/**
 * Used by Objects stored in SafeLists that want to remove themselves
 * from the list when they die. SafeList nodes will add themselves as
 * a termination listener upon storing an Object that implements this
 * interface.
 * 
 * Protip: use a SafeList to store TerminateListeners so as to prevent
 * concurrent modification.
 * 
 * @see util.Node
 * @author Matt Crow
 */
public interface Terminable {
    public void addTerminationListener(TerminationListener listen);
    
    /**
     * Iterate through TerminateListeners,
     * call each of their objectWasTerminated(this)
     * methods
     */
    public void terminate();
}
