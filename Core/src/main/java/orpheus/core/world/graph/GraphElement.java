package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.JsonObject;

/**
 * A simplified representation of an object which will be sent to remote clients
 * and rendered on a canvas.
 */
public interface GraphElement {

    /**
     * Renders this object on the given Java Swing graphics context. I may want
     * to migrate this functionality to a seperate class in the Client project,
     * as rendering the object is not a concern of the server
     * 
     * @param g the graphics context to render on
     */
    public void draw(Graphics g);

    /**
     * Implementing classes should override this method
     * to insert at least everything that is passed to their
     * constructor to a JsonObject, as they will be reconstructed
     * from that JsonObject.
     * @return a JSON representation of this
     */
    public abstract JsonObject toJson();
}