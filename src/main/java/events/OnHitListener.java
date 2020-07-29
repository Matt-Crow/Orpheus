package events;

import java.io.Serializable;

/**
 * Used to respond to when an Entity is hit by another.
 */
public interface OnHitListener extends Serializable{
    public void trigger(OnHitEvent e);
}
