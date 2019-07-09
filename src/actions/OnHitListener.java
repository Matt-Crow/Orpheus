package actions;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Used to respond to when an Entity is hit by another.
 */
public class OnHitListener extends Triggerable<OnHitEvent> implements Serializable{

    public OnHitListener(int use, Consumer<OnHitEvent> function) {
        super(use, function);
    }
    
    public OnHitListener(Consumer<OnHitEvent> function){
        this(0, function);
    }
}
