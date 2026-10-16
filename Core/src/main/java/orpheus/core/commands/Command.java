package orpheus.core.commands;

import javax.json.JsonObject;

import world.World;

/**
 * A Command is a message which can be sent to the server for remote execution,
 * or by a local client in solo.
 */
public interface Command {
    
    /**
     * A command executor should call this method.
     */
    public void executeIn(World world);

    /**
     * Implementing classes should override this method
     * to insert at least everything that is passed to their
     * constructor to a JsonObject, as they will be reconstructed
     * from that JsonObject.
     * @return a JSON representation of this
     */
    public abstract JsonObject toJson();
}
