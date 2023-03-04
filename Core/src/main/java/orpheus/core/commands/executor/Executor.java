package orpheus.core.commands.executor;

import orpheus.core.commands.Command;

/**
 * Executes commands either locally or remotely on a world
 */
public interface Executor {
    
    /**
     * Executes the given command on a world.
     * @param command the command to execute
     */
    public void execute(Command command);
}
