package orpheus.core.commands.executor;

import orpheus.core.commands.Command;
import world.World;

/**
 * Executes commands on a world
 */
public class LocalExecutor implements Executor {

    /**
     * The world to execute commands on
     */
    private final World target;

    public LocalExecutor(World target) {
        this.target = target;
    }

    @Override
    public void execute(Command command) {
        command.executeIn(target);
    }
}
