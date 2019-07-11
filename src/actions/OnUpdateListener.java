package actions;

import java.io.Serializable;

/**
 * Used to react to when an Entity is updated.
 */
public interface OnUpdateListener extends Serializable{
    
    /**
     * Triggered at the end of every frame.
     * @param e an event detailing what Entity was updated.
     * @see ActionRegister
     */
    public abstract void trigger(OnUpdateEvent e);
}
