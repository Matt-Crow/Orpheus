package controls.ai;

/**
 * This is the base class for applying AI to non-user entities.
 * 
 * @author Matt Crow
 * @param <TargetType> the type of Object this Behavior is applied to
 */
public abstract class AbstractBehavior<TargetType> {
    private final TargetType target;
    
    public AbstractBehavior(TargetType target){
        this.target = target;
    }

    protected TargetType getTarget(){
        return target;
    }
    
    /**
     * Applies this behavior to its target.
     * 
     * @return the new Behavior for the target to transition to 
     */
    public abstract AbstractBehavior<TargetType> update();
}
