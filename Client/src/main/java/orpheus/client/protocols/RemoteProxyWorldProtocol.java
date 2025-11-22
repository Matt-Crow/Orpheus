package orpheus.client.protocols;

import net.OrpheusClient;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.MessageHandler;
import orpheus.client.gui.pages.play.RemoteWorldSupplier;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends MessageHandler {
    private final RemoteWorldSupplier worldSupplier;
    
    public RemoteProxyWorldProtocol(OrpheusClient runningServer, RemoteWorldSupplier worldSupplier){
        super(runningServer);
        this.worldSupplier = worldSupplier;
        addHandler(ServerMessageType.WORLD, this::receiveWorld);
    }

    private void receiveWorld(ServerMessagePacket sm) {
        var json = sm.getMessage().getBody();
        var world = orpheus.core.world.graph.World.fromJson(json);
        worldSupplier.setWorld(world);
    }
}
