package orpheus.client.protocols;

import net.messages.ServerMessageType;
import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.core.commands.Command;
import orpheus.core.commands.executor.Executor;
import orpheus.core.net.OrpheusClient;
import orpheus.core.net.messages.Message;
import orpheus.core.net.protocols.MessageHandler;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends MessageHandler implements Executor {
    private final OrpheusClient client;
    private final WorldGraphSupplier worldSupplier;

    public RemoteProxyWorldProtocol(
        OrpheusClient runningServer, 
        WorldGraphSupplier worldSupplier
    ){
        client = runningServer;
        this.worldSupplier = worldSupplier;
        addHandler(ServerMessageType.WORLD, this::receiveWorld);
    }

    private void receiveWorld(Message sm) {
        var json = sm.getBody();
        var world = orpheus.core.world.graph.World.fromJson(json);
        worldSupplier.setWorld(world);
    }

    @Override
    public void execute(Command command) {
        client.send(new Message(ServerMessageType.CONTROL_PRESSED, command.toJson()));
    }
}
