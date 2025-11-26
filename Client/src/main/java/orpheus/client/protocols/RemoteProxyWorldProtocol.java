package orpheus.client.protocols;

import net.OrpheusClient;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.MessageHandler;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import orpheus.client.gui.pages.play.RemoteWorldSupplier;
import orpheus.core.utils.timer.FrameTimer;
import orpheus.core.world.graph.particles.Particles;

/**
 * The RemoteProxyProtocol is used by clients to receive serialized 
 * WorldContent from the host, then render it on their local machine.
 * 
 * @author Matt Crow
 */
public class RemoteProxyWorldProtocol extends MessageHandler {
    private final RemoteWorldSupplier worldSupplier;
    private final FrameTimer updater;

    public RemoteProxyWorldProtocol(
        OrpheusClient runningServer, 
        RemoteWorldSupplier worldSupplier,
        HeadsUpDisplay hud,
        Particles particles
    ){
        super(runningServer);
        this.worldSupplier = worldSupplier;
        addHandler(ServerMessageType.WORLD, this::receiveWorld);

        /**
         * We now have the world,
         * but the server does not update particles,
         * so we have to update those ourself
         */
        updater = new FrameTimer();
        updater.addEndOfFrameListener(hud::frameEnded);
        updater.addEndOfFrameListener(e -> {
            worldSupplier.get().spawnParticlesInto(particles);
            particles.update();
        });
    }

    private void receiveWorld(ServerMessagePacket sm) {
        var json = sm.getMessage().getBody();
        var world = orpheus.core.world.graph.World.fromJson(json);
        worldSupplier.setWorld(world);
    }

    @Override
    public void handleStart() {
        updater.start();
    }

    @Override
    public void handleStop() {
        updater.stop();
    } 
}
