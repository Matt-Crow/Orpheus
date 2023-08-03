package orpheus.core.utils;

/**
 * Can be applied to anything with a unique name
 */
public interface Prototype {
    
    /**
     * @return a unique identifier for this object
     */
    public String getName();

    /**
     * @return a deep copy of this
     */
    public Prototype copy();
}
