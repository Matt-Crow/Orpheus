package gui.pages;

import java.io.Serializable;

/**
 * Used by WorldUpdaters,
 * fired at the end of every frame.
 * 
 * @author Matt Crow
 */
public interface EndOfFrameListener extends Serializable{
    public void frameEnded();
}
