package orpheus.client.protocols;

import orpheus.client.gui.pages.PlayerControls;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import orpheus.client.gui.pages.play.RemoteWorldSupplier;
import orpheus.client.gui.pages.play.RemoteWorldUpdater;

import javax.json.JsonObject;
import javax.json.JsonValue;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.client.gui.pages.worldselect.WaitingRoom;
import orpheus.core.commands.executor.RemoteExecutor;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;
import orpheus.core.world.graph.World;
import orpheus.core.world.graph.particles.Particles;

import java.io.StringReader;

import javax.json.Json;

import net.AbstractNetworkClient;
import net.OrpheusClient;
import net.protocols.AbstractWaitingRoomProtocol;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends AbstractWaitingRoomProtocol {

    private final WaitingRoom room;

    public WaitingRoomClientProtocol(OrpheusClient runningServer, WaitingRoom linkedRoom) {
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
                addToTeamProto(u);
            }
        });
        room.updateTeamDisplays();
    }

    private void receiveUpdate(ServerMessagePacket sm) {
        JsonObject json = Json.createReader(
            new StringReader(sm.getMessage().getBodyText())
        ).readObject();
        addToTeamProto(User.fromJson(json));
        room.updateTeamDisplays();
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
        getServer().setProtocol(protocol);

        canvas.start();
        
        // todo also allow this to stop
        var updater = new RemoteWorldUpdater(worldSupplier, particles);
        updater.addEndOfFrameListener(hud);
        updater.start();
    }
}
