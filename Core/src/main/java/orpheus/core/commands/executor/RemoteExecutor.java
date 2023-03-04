package orpheus.core.commands.executor;

import net.OrpheusClient;
import net.messages.ServerMessageType;
import orpheus.core.commands.Command;
import orpheus.core.net.messages.Message;

/**
 * Sends a command to a server so it can execute it on a remote world
 */
public class RemoteExecutor implements Executor {

    private final OrpheusClient client;

    public RemoteExecutor(OrpheusClient client) {
        this.client = client;
    }

    @Override
    public void execute(Command command) {
        client.send(new Message(ServerMessageType.CONTROL_PRESSED, command.toJson()));
    }
}
