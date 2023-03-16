package net.protocols;

/**
 * Used by WorldUpdaters,
 * fired at the end of every frame.
 * 
 * @author Matt Crow
 */
public interface EndOfFrameListener {
    public void frameEnded();
}
