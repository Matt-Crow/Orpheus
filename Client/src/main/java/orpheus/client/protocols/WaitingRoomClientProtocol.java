package orpheus.client.protocols;

import orpheus.client.gui.pages.PlayerControls;
import orpheus.client.gui.pages.play.HeadsUpDisplay;
import javax.json.JsonObject;
import javax.json.JsonValue;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.client.gui.pages.worldselect.WaitingRoomPage;
import orpheus.core.commands.executor.RemoteExecutor;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;
import orpheus.core.world.graph.World;
import orpheus.core.world.graph.particles.Particles;
import java.util.Optional;

import net.AbstractNetworkClient;
import net.OrpheusClient;
import net.protocols.MessageHandler;
import net.protocols.WaitingRoom;

/**
 * Allows clients to handle messages from the server while in the waiting room.
 * @author Matt Crow
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
        var obj = sm.getMessage().getBody();
        obj.getJsonArray("team").stream().forEach((jv) -> {
            if (jv.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                User u = User.fromJson((JsonObject) jv);
                waitingRoom.addUser(u);
            }
        });
        room.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    private void receiveUpdate(ServerMessagePacket sm) {
        waitingRoom.addUser(User.fromJson(sm.getMessage().getBody()));
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

    private void receiveWorld(ServerMessagePacket sm) {
        // Kind of a mess, see issue #64
        var world = World.fromJson(sm.getMessage().getBody());
        var me = room.getContext().getLoggedInUser();
        var worldSupplier = WorldGraphSupplier.fromGraph(world);
        var hud = new HeadsUpDisplay(worldSupplier, me.getId());
        var newPage = new WorldPage(room.getContext(), room.getHost(), hud);
        var server = getServer();
        var particles = new Particles();
        var canvas = new WorldCanvas(
            worldSupplier,
            particles,
            new PlayerControls(me.getId(), new RemoteExecutor(server))
        );
        newPage.setCanvas(canvas);
        
        room.getHost().switchToPage(newPage);
        
        // configure the backend
        var protocol = new RemoteProxyWorldProtocol(
            getServer(),
            worldSupplier,
            hud,
            particles
        );
        server.setMessageHandler(Optional.of(protocol));
        server.setChatProtocol(new ClientChatProtocol(me, server, newPage.getChatBox()));
    }
}
