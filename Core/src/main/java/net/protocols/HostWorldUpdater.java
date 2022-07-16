package net.protocols;

import net.protocols.AbstractWorldUpdater;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import serialization.WorldSerializer;
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
        hostingServer.send(new ServerMessage(
            new WorldSerializer(world).serializeToString(),
            ServerMessageType.WORLD_UPDATE
        ));
    }
}
