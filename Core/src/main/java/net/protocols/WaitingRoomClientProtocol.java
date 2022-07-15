package net.protocols;

import controls.PlayerControls;
import gui.pages.worldPlay.RemoteWorldUpdater;
import world.build.BuildJsonUtil;
import javax.json.JsonObject;
import javax.json.JsonValue;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
import gui.pages.worldPlay.WorldCanvas;
import gui.pages.worldPlay.WorldPage;
import gui.pages.worldSelect.WaitingRoom;
import java.io.IOException;
import java.io.StringReader;
import javax.json.Json;

import net.AbstractNetworkClient;
import net.OrpheusClient;
import net.messages.ServerMessage;
import serialization.WorldSerializer;
import start.RemoteOrpheusClient;
import world.World;
import world.WorldBuilder;
import world.WorldBuilderImpl;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends AbstractWaitingRoomProtocol {

    private final WaitingRoom room;

    public WaitingRoomClientProtocol(OrpheusClient runningServer, WaitingRoom linkedRoom) {
        super(runningServer);
        this.room = linkedRoom;
    }

    @Override
    public OrpheusClient getServer(){
        AbstractNetworkClient anc = super.getServer();
        if(!(anc instanceof OrpheusClient)){
            throw new UnsupportedOperationException("WaitingRoomClientProtocol can only run on an OrpheusClient");
        }
        return (OrpheusClient)anc;
    }

    @Override
    public boolean receive(ServerMessagePacket sm) {
        boolean received = true;
        switch (sm.getMessage().getType()) {
            case WAITING_ROOM_INIT:
                receiveInit(sm);
                break;
            case WAITING_ROOM_UPDATE:
                receiveUpdate(sm);
                break;
            case REQUEST_PLAYER_DATA: {
                try {
                    receiveBuildRequest(sm);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            break;
            case NOTIFY_IDS:
                receiveRemoteId(sm);
                break;
            case WORLD_INIT:
                receiveWorldInit(sm);
                break;
            default:
                received = false;
                break;
        }
        return received;
    }

    private void receiveInit(ServerMessagePacket sm) {
        JsonObject obj = JsonUtil.fromString(sm.getMessage().getBody());
        JsonUtil.verify(obj, "team");
        obj.getJsonArray("team").stream().forEach((jv) -> {
            if (jv.getValueType().equals(JsonValue.ValueType.OBJECT)) {
                AbstractUser u = AbstractUser.deserializeJson((JsonObject) jv);
                addToTeamProto(u);
            }
        });
        room.updateTeamDisplays();
    }

    private void receiveUpdate(ServerMessagePacket sm) {
        JsonObject json = Json.createReader(
                new StringReader(
                        sm.getMessage().getBody()
                )
        ).readObject();
        addToTeamProto(AbstractUser.deserializeJson(json));
        room.updateTeamDisplays();
    }

    public final void requestStart() {
        getServer().send(new ServerMessage("start", ServerMessageType.START_WORLD));
    }

    private synchronized void receiveBuildRequest(ServerMessagePacket sm) throws IOException {
        getServer().send(new ServerMessage(
                BuildJsonUtil.serializeJson(room.getSelectedBuild()).toString(),
                ServerMessageType.PLAYER_DATA
        ));
        room.setInputEnabled(false);
    }

    private void receiveRemoteId(ServerMessagePacket sm) {
        LocalUser.getInstance().setRemotePlayerId(sm.getMessage().getBody());
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
    private void receiveWorldInit(ServerMessagePacket sm) {
        WorldBuilder builder = new WorldBuilderImpl();

        // don't like having to do static like this
        World entireWorld = builder
                .withContent((WorldContent) WorldSerializer.fromSerializedString(sm.getMessage().getBody()))
                .build();

        LocalUser me = LocalUser.getInstance();

        RemoteOrpheusClient orpheus = new RemoteOrpheusClient(me, getServer());
        WorldPage p = new WorldPage(room.getHost());
        WorldCanvas renderer = new WorldCanvas(
                entireWorld,
                new PlayerControls(entireWorld, me.getRemotePlayerId(), orpheus),
                false
        );
        p.setCanvas(renderer);
        room.getHost().switchToPage(p);

        RemoteProxyWorldProtocol protocol = new RemoteProxyWorldProtocol(
                getServer(),
                entireWorld
        );
        getServer().setProtocol(protocol);

        renderer.start();
        RemoteWorldUpdater updater = new RemoteWorldUpdater(entireWorld);
        updater.start();
    }
}
