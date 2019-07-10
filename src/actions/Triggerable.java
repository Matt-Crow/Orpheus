package actions;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.SafeList;

/**
 * The Triggerable class is meant to replace most of
 * the "trigger this when something happens, then eventually
 * delete this" type effects that occur in Orpheus (statuses, actions, etc)
 * 
 * A Triggerable is basically a Consumer which will only run a given 
 * number of times before terminating. You will want to store Triggerables
 * in something that implements the TerminateListener interface so that it
 * can automatically remove this once it has terminated.
 * 
 * Most simply, you can add it to a SafeList, which automatically handles
 * terminables.
 * 
 * @see TerminateListener
 * @see util.SafeList
 * @see util.Node
 * @author Matt Crow
 * @param <T> the type which will be fed into this in the trigger method.
 */
public class Triggerable<T> implements Terminable, Serializable {
    private boolean shouldTerminate;
    private final int maxUses;
    private int usesLeft;
    private Consumer<T> f;
    
    private final SafeList<TerminateListener> termListens;
    
    public Triggerable(int use, Consumer<T> function){
        maxUses = use;
        usesLeft = use;
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
    
    public Triggerable(int i){
        this(i, (o)->{
            try {
                throw new Exception("Function for this Triggerable not set");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    
    public Triggerable(){
        this(0);
    }
    
    /**
     * Use for AbstractStatus so I don't have to
     * pass complex lambdas to the constructor
     * @param func 
     */
    public void setFunction(Consumer<T> func){
        f = func;
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
    
    public void reset(){
        shouldTerminate = false;
        usesLeft = maxUses;
    }
    
    public void trigger(T param){
        if(shouldTerminate){
            System.err.println("Don't bother me, I'm dead!");
            throw new UnsupportedOperationException("Cannot trigger a Triggerable which has already terminated");
        }
        f.accept(param);
        usesLeft--;
        
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
    public void addTerminationListener(TerminateListener listen) {
        termListens.add(listen);
    }

    @Override
    public boolean removeTerminationListener(TerminateListener listen) {
        return termListens.remove(listen);
    }

    @Override
    public void terminate() {
        shouldTerminate = true;
        termListens.forEach((TerminateListener tl)->tl.objectWasTerminated(this));
    }
    
    
    public static void main(String[] args){
        SafeList<Triggerable<Integer>> sti = new SafeList<>();
        sti.add(new Triggerable(3, (i)->{
            System.out.println("I should only run 3 times!");
        }));
        sti.add(new Triggerable((i)->{
            System.out.println("But pie is forever.");
        }));
        sti.add(new Triggerable(1, (i)->{
            System.out.println("urk!");
        }));
        for(int i = 0; i < 10; i++){
            System.out.println("Round " + i);
            sti.forEach((cti)->{
                cti.trigger(0);
            });
        }
    }
}
