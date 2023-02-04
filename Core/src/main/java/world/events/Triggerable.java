package world.events;

import java.io.Serializable;
import java.util.function.Consumer;
import util.SafeList;
import world.events.termination.TerminationListener;

/**
 * A Triggerable is an event listener that can only run up to a given number of
 * times, then effectively deletes itself.
 * 
 * Most simply, you can add it to a SafeList, which automatically handles
 * terminables.
 * 
 * @see util.SafeList
 * @author Matt Crow
 * @param <T> the type which will be fed into this in the trigger method.
 */
public class Triggerable<T> implements Terminable, Serializable, world.events.termination.Terminable {
    private boolean shouldTerminate;
    private final int maxUses;
    private int usesLeft;
    private int timesTriggered;
    private final Consumer<T> f;
    
    private final SafeList<TerminationListener> termListens;
    
    public Triggerable(int use, Consumer<T> function){
        maxUses = use;
        usesLeft = use;
        timesTriggered = 0;
        shouldTerminate = false;
        f = function;
        
        termListens = new SafeList<>();
    }
    
    /**
     * Creates a permenent
     * triggerable, one with
     * infinite duration
     * @param function the Consumer to run whenever this is triggered
     */
    public Triggerable(Consumer<T> function){
        this(0, function);
    }
    
    public boolean getShouldTerminate(){
        return shouldTerminate;
    }
    
    public int getMaxUses(){
        return maxUses;
    }
    public int getUsesLeft(){
        return usesLeft;
    }
    public int getTimesTriggered(){
        return timesTriggered;
    }
    
    public void reset(){
        shouldTerminate = false;
        usesLeft = maxUses;
        timesTriggered = 0;
    }
    
    public void trigger(T param){
        if(shouldTerminate){
            System.err.println("Don't bother me, I'm dead!");
            throw new UnsupportedOperationException("Cannot trigger a Triggerable which has already terminated");
        }
        f.accept(param);
        usesLeft--;
        ++timesTriggered;
        
        //note that uses left of 0 or less will trigger infinite times
        if(usesLeft == 0){
            shouldTerminate = true;
            terminate();
        }
        
        //this way it doesn't roll over
        if(usesLeft < 0){
            usesLeft = 0;
        }
    }

    @Override
    public void addTerminationListener(TerminationListener listen) {
        termListens.add(listen);
    }

    @Override
    public void terminate() {
        shouldTerminate = true;
        termListens.forEach((TerminationListener tl)->tl.objectWasTerminated(this));
        termListens.clear();
    }

    @Override
    public boolean isTerminating() {
        return usesLeft == 0; // don't use <= 0, as infinite durations use -1
    }
}
