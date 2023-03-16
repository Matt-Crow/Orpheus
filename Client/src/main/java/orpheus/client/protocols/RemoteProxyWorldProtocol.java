package orpheus.client.protocols;

import net.OrpheusClient;
import net.messages.ServerMessagePacket;
import net.protocols.AbstractProtocol;
import orpheus.client.gui.pages.play.RemoteWorldSupplier;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends AbstractProtocol{
    private final RemoteWorldSupplier worldSupplier;
    
    public RemoteProxyWorldProtocol(OrpheusClient runningServer, RemoteWorldSupplier worldSupplier){
        super(runningServer);
        this.worldSupplier = worldSupplier;
    }

    private void receiveWorld(ServerMessagePacket sm) {
        var json = sm.getMessage().getBody();
        var world = orpheus.core.world.graph.World.fromJson(json);
        worldSupplier.setWorld(world);
    }
    
    @Override
    public boolean receive(ServerMessagePacket sm) {
        boolean handled = true;
        switch(sm.getMessage().getType()){
            case WORLD:
                receiveWorld(sm);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
}
