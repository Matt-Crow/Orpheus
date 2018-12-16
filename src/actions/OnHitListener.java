package actions;

/**
 * Used to respond to when an Entity is hit by another.
 */
public interface OnHitListener {
    
    /**
     * Triggered whenever an Entity is hit by another.
     * @param e an event, detailing who was hit by who.
     * @see ActionRegister.
     */
    public void actionPerformed(OnHitEvent e);
}
