package orpheus.core.world.graph;

import java.awt.Graphics;

import serialization.JsonSerialable;

/**
 * A simplified representation of an object which will be sent to remote clients
 * and rendered on a canvas.
 */
public interface GraphElement extends JsonSerialable {

    /**
     * Renders this object on the given Java Swing graphics context. I may want
     * to migrate this functionality to a seperate class in the Client project,
     * as rendering the object is not a concern of the server
     * 
     * @param g the graphics context to render on
     */
    public void draw(Graphics g);
}