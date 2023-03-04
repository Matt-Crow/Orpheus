package orpheus.core.commands;

import serialization.JsonSerialable;
import world.World;

/**
 * A Command is a message which can be sent to the server for remote execution,
 * or by a local client in solo.
 */
public interface Command extends JsonSerialable {
    
    /**
     * A command executor should call this method.
     */
    public void executeIn(World world);
}
