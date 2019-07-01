package actions;

import java.io.Serializable;
import java.util.function.Consumer;
import util.Random;

/**
 * The Triggerable class is meant to replace most of
 * the "trigger this when something happens, then eventually
 * delete this" type effects that occur in Orpheus (statuses, actions, etc)
 * 
 * @author Matt Crow
 * @param <T> the type which will be fed into this in the trigger method.
 */
public class Triggerable<T> implements Serializable {
    private boolean shouldTerminate;
    private final int maxUses;
    private int usesLeft;
    private final Consumer<T> f;
    
    public Triggerable(int use, Consumer<T> function){
        maxUses = use;
        usesLeft = use;
        shouldTerminate = false;
        f = function;
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
    
    public void reset(){
        shouldTerminate = false;
        usesLeft = maxUses;
    }
    
    public void trigger(T param){
        f.accept(param);
        usesLeft--;
        
        //note that uses left of 0 or less will trigger infinite times
        if(usesLeft == 0){
            shouldTerminate = true;
        }
        
        //this way it doesn't roll over
        if(usesLeft < 0){
            usesLeft = 0;
        }
    }
    
    public static void main(String[] args){
        Triggerable<Integer> t = new Triggerable(3, System.out::println);
        while(!t.getShouldTerminate()){
            t.trigger(Random.choose(0, 100));
        }
    }
}
