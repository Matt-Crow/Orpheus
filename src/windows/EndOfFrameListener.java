package windows;

import java.io.Serializable;

/**
 * Used by Canvases,
 * fired at the end of every frame.
 * 
 * @author Matt Crow
 */
public interface EndOfFrameListener extends Serializable{
    public void frameEnded(Canvas c);
}
