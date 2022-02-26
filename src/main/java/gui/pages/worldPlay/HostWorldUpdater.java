package gui.pages.worldPlay;

import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import util.SerialUtil;
import world.TempWorld;

/**
 *
 * @author Matt Crow
 */
public class HostWorldUpdater extends AbstractWorldUpdater {
    private final OrpheusServer hostingServer;
    
    public HostWorldUpdater(OrpheusServer hostingServer, TempWorld world) {
        super(world, false);
        this.hostingServer = hostingServer;
    }

    @Override
    protected void updateWorld(TempWorld world) {
        world.update();
        hostingServer.send(new ServerMessage(
            SerialUtil.serializeToString(world.getContent()),
            ServerMessageType.WORLD_UPDATE
        ));
    }
}
