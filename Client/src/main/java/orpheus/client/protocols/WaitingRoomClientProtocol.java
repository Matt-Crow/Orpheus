package orpheus.client.protocols;

import javax.json.JsonObject;
import javax.json.JsonValue;

import serialization.JsonUtil;
import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.client.gui.pages.worldselect.WaitingRoomPage;
import orpheus.core.net.*;
import orpheus.core.net.protocols.*;
import orpheus.core.users.User;
import orpheus.core.world.graph.World;
import java.io.StringReader;
import java.util.Optional;

import javax.json.Json;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends MessageHandler {
    private final OrpheusClient client;
    private final WaitingRoomPage frontEnd;
    private final WaitingRoom waitingRoom = new WaitingRoom();

    public WaitingRoomClientProtocol(OrpheusClient runningServer, WaitingRoomPage frontEnd) {
        this.client = runningServer;
        this.frontEnd = frontEnd;
        addHandler(ServerMessageType.WAITING_ROOM_INIT, this::receiveInit);
        addHandler(ServerMessageType.WAITING_ROOM_UPDATE, this::receiveUpdate);
        addHandler(ServerMessageType.REQUEST_PLAYER_DATA, this::receiveBuildRequest);
        addHandler(ServerMessageType.WORLD, this::receiveWorld);
    }

    private void receiveInit(Message sm) {
        JsonObject obj = JsonUtil.fromString(sm.getBodyText());
        JsonUtil.verify(obj, "team");
        obj.getJsonArray("team").stream().forEach((jv) -> {
            if (jv.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                User u = User.fromJson((JsonObject) jv);
                waitingRoom.addUser(u);
            }
        });
        frontEnd.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    private void receiveUpdate(Message sm) {
        JsonObject json = Json.createReader(
            new StringReader(sm.getBodyText())
        ).readObject();
        waitingRoom.addUser(User.fromJson(json));
        frontEnd.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    public final void requestStart() {
        client.send(new Message("start", ServerMessageType.START_WORLD));
    }

    private synchronized void receiveBuildRequest(Message sm) {
        var selectedBuild = frontEnd.getSelectedSpecification();
        var json = selectedBuild.get().toJson();
        client.send(new Message(
                json.toString(),
                ServerMessageType.PLAYER_DATA
        ));
        frontEnd.setInputEnabled(false);
    }

    private void receiveWorld(Message sm) {
        // set up the model
        var world = World.fromJson(sm.getBody());
        var worldSupplier = WorldGraphSupplier.fromGraph(world);
        var me = frontEnd.getContext().getLoggedInUser();

        // configure the backend
        var protocol = new RemoteProxyWorldProtocol(client, worldSupplier);
        client.setMessageHandler(Optional.of(protocol));

        // set up the new page
        var newPage = new WorldPage(
            frontEnd.getContext(), 
            frontEnd.getHost(), 
            worldSupplier,
            me.getId(),
            protocol
        );
        newPage.getChatBox().handleChatMessagesFor(client);

        frontEnd.getHost().switchToPage(newPage);
    }
}
