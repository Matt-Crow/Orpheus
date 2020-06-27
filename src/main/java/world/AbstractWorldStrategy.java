package world;

/**
 *
 * @author Matt
 */
public abstract class AbstractWorldStrategy {
    private final World world;
    
    public AbstractWorldStrategy(World forWorld){
        world = forWorld;
    }
    
    public abstract void update();
}
