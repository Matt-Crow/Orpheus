package orpheus.client.protocols;

import orpheus.client.gui.pages.PlayerControls;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import orpheus.client.gui.pages.play.RemoteWorldSupplier;
import javax.json.JsonObject;
import javax.json.JsonValue;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.client.gui.pages.worldselect.WaitingRoomPage;
import orpheus.core.commands.executor.RemoteExecutor;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;
import orpheus.core.utils.timer.FrameTimer;
import orpheus.core.world.graph.World;
import orpheus.core.world.graph.particles.Particles;
import java.io.StringReader;
import java.util.Optional;

import javax.json.Json;

import net.AbstractNetworkClient;
import net.OrpheusClient;
import net.protocols.MessageHandler;
import net.protocols.WaitingRoom;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends MessageHandler {

    private final WaitingRoomPage room;
    private final WaitingRoom waitingRoom = new WaitingRoom();

    public WaitingRoomClientProtocol(OrpheusClient runningServer, WaitingRoomPage linkedRoom) {
        super(runningServer);
        this.room = linkedRoom;
        addHandler(ServerMessageType.WAITING_ROOM_INIT, this::receiveInit);
        addHandler(ServerMessageType.WAITING_ROOM_UPDATE, this::receiveUpdate);
        addHandler(ServerMessageType.REQUEST_PLAYER_DATA, this::receiveBuildRequest);
        addHandler(ServerMessageType.WORLD, this::receiveWorld);
    }

    @Override
    public OrpheusClient getServer(){
        AbstractNetworkClient anc = super.getServer();
        if(!(anc instanceof OrpheusClient)){
            throw new UnsupportedOperationException("WaitingRoomClientProtocol can only run on an OrpheusClient");
        }
        return (OrpheusClient)anc;
    }

    private void receiveInit(ServerMessagePacket sm) {
        JsonObject obj = JsonUtil.fromString(sm.getMessage().getBodyText());
        JsonUtil.verify(obj, "team");
        obj.getJsonArray("team").stream().forEach((jv) -> {
            if (jv.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                User u = User.fromJson((JsonObject) jv);
                waitingRoom.addUser(u);
            }
        });
        room.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    private void receiveUpdate(ServerMessagePacket sm) {
        JsonObject json = Json.createReader(
            new StringReader(sm.getMessage().getBodyText())
        ).readObject();
        waitingRoom.addUser(User.fromJson(json));
        room.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    public final void requestStart() {
        getServer().send(new Message("start", ServerMessageType.START_WORLD));
    }

    private synchronized void receiveBuildRequest(ServerMessagePacket sm) {
        var selectedBuild = room.getSelectedSpecification();
        var json = selectedBuild.get().toJson();
        getServer().send(new Message(
                json.toString(),
                ServerMessageType.PLAYER_DATA
        ));
        room.setInputEnabled(false);
    }

    /**
     * allows remote users to receive and de-serialize the AbstractWorld created
     * by the host.
     *
     * this method is currently having problems, as the enemy team might not
     * serialize, and it takes a couple seconds to load teams into the world
     *
     * @param sm
     */
    private void receiveWorld(ServerMessagePacket sm) {
        var world = World.fromJson(sm.getMessage().getBody());
        var me = room.getContext().getLoggedInUser();
        var worldSupplier = new RemoteWorldSupplier(world);
        var hud = new HeadsUpDisplay(worldSupplier, me.getId());
        var p = new WorldPage(room.getContext(), room.getHost(), hud);
        var particles = new Particles();
        var canvas = new WorldCanvas(
            worldSupplier,
            particles,
            new PlayerControls(me.getId(), new RemoteExecutor(getServer()))
        );
        p.setCanvas(canvas);

        getServer().setChatProtocol(new ClientChatProtocol(me, getServer(), p.getChatBox()));

        room.getHost().switchToPage(p);

        RemoteProxyWorldProtocol protocol = new RemoteProxyWorldProtocol(
            getServer(),
            worldSupplier
        );
        getServer().setMessageHandler(Optional.of(protocol));

        canvas.start();
        

        /**
         * We now have the world,
         * but the server does not update particles,
         * so we have to update those ourself
         */

        // todo also allow this to stop
        // probably make protocols disposable, call that method when switching off of them
        var updater = new FrameTimer();
        updater.addEndOfFrameListener(hud::frameEnded);
        updater.addEndOfFrameListener(e -> {
            worldSupplier.get().spawnParticlesInto(particles);
            particles.update();
        });
        updater.start();
    }
}
