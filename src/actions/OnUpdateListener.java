package actions;

/**
 * Used to react to when an Entity is updated.
 */
public interface OnUpdateListener{
    
    /**
     * Triggered at the end of every frame.
     * @param a an event detailing what Entity was updated.
     * @see ActionRegister
     */
    public abstract void actionPerformed(OnUpdateEvent a);
}
