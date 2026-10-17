package orpheus.client.protocols;

import orpheus.client.gui.pages.play.WorldGraphSupplier;
import orpheus.client.gui.pages.play.WorldPage;
import orpheus.client.gui.pages.worldselect.WaitingRoomPage;
import orpheus.core.net.*;
import orpheus.core.net.protocols.*;
import orpheus.core.users.User;
import orpheus.core.utils.OrpheusJsonHelper;
import orpheus.core.world.graph.World;
import java.util.Optional;

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
        var obj = OrpheusJsonHelper.fromString(sm.getBodyText());
        var waitingRoomInitMessage = WaitingRoomInitMessage.fromJson(obj);
        waitingRoomInitMessage.getUsers().forEach(waitingRoom::addUser);
        frontEnd.updateTeamDisplays(waitingRoom.getAllUsers());
    }

    private void receiveUpdate(Message sm) {
        var json = OrpheusJsonHelper.fromString(sm.getBodyText());
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
