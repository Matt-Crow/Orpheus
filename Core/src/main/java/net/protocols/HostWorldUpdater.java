package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessageType;
import orpheus.core.net.messages.Message;
import world.World;

/**
 * used in conjunction with HostWorldProtocol to 
 * 1. Update local world contents, 
 * 2. serialize and send those contents to all clients so they can update their proxies,
 * 3. while the protocol handles receiving controls from remote players.
 * @author Matt Crow
 */
public class HostWorldUpdater extends AbstractWorldUpdater {
    private final OrpheusServer hostingServer;
    
    public HostWorldUpdater(OrpheusServer hostingServer, World world) {
        super(world, false);
        this.hostingServer = hostingServer;
    }

    @Override
    protected void updateWorld(World world) {
        world.update();
        
        hostingServer.send(new Message(
            ServerMessageType.WORLD, 
            world.toGraph().serializeJson()
        ));
    }
}
