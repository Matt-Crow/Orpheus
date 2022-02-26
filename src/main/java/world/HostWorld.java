package world;

import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import util.SerialUtil;

/**
 * The HostWorld is used in conjunction with
 * HostWorldProtocol and HostWorldUpdater to 
 * 1. Update local world contents, 
 * 2. serialize and send those contents to all clients so they can update their proxies,
 * 3. while the protocol handles receiving controls from remote players.
 * @author Matt
 */
public class HostWorld extends AbstractWorldShell{
    private final OrpheusServer hostingServer;
    
    public HostWorld(OrpheusServer hostingServer) {
        super();
        this.hostingServer = hostingServer;
    }
    
    /**
     * Updates the local world,
     * serializes it, and sends 
     * it to all connected clients
     * so they can update their proxies.
     */
    @Override
    public void update() {
        hostingServer.send(new ServerMessage(
            SerialUtil.serializeToString(getTempWorld().getContent()),
            ServerMessageType.WORLD_UPDATE
        ));
    }

}
