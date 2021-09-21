package net.protocols;

import controls.userControls.RemotePlayerControls;
import world.customizables.BuildJsonUtil;
import java.io.IOException;
import javax.json.JsonObject;
import javax.json.JsonValue;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
import gui.pages.worldSelect.AbstractWaitingRoom;
import gui.pages.worldPlay.WorldCanvas;
import gui.pages.worldPlay.WorldPage;
import net.messages.ServerMessage;
import world.RemoteProxyWorld;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends AbstractWaitingRoomProtocol{
    public WaitingRoomClientProtocol(OrpheusServer runningServer, AbstractWaitingRoom linkedRoom) {
        super(runningServer, linkedRoom);
        resetTeamProto();
    }
    
    private void receiveInit(ServerMessagePacket sm){
        resetTeamProto();
        JsonObject obj = JsonUtil.fromString(sm.getMessage().getBody());
        JsonUtil.verify(obj, "team");
        obj.getJsonArray("team").stream().forEach((jv)->{
            if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                AbstractUser u = AbstractUser.deserializeJson((JsonObject)jv);
                addToTeamProto(u);
            }
        });
        addToTeamProto(LocalUser.getInstance()); // sm doesn't contain this user, so I need to manually add myself to the displays
    }
    
    private void receiveUpdate(ServerMessagePacket sm){
        addToTeamProto(sm.getSender());
    }
    
    private synchronized void receiveBuildRequest(ServerMessagePacket sm){
        getServer().send(new ServerMessage(
            BuildJsonUtil.serializeJson(getFrontEnd().getSelectedBuild()).toString(),
            ServerMessageType.PLAYER_DATA
        ), sm.getSender());
        getFrontEnd().setInputEnabled(false);
    }
    
    private void receiveRemoteId(ServerMessagePacket sm){
        LocalUser.getInstance().setRemotePlayerId(sm.getMessage().getBody());
    }
    
    /**
     * allows remote users to receive and de-serialize the AbstractWorld created by the host.
     * 
     * this method is currently having problems, as the enemy team might not serialize,
     * and it takes a couple seconds to load teams into the world
     * 
     * @param sm 
     */
    private void receiveWorldInit(ServerMessagePacket sm){
        WorldContent w = WorldContent.fromSerializedString(sm.getMessage().getBody());
        RemoteProxyWorld world = new RemoteProxyWorld(sm.getSendingSocket().getInetAddress(), w);
        w.setShell(world);
        
        LocalUser me = LocalUser.getInstance();
        //me.linkToRemotePlayerInWorld(world);
        world.createCanvas();
        w.init(); // do I need this?
        
        WorldPage p = new WorldPage();
        WorldCanvas canv = world.getCanvas();
        canv.addPlayerControls(new RemotePlayerControls(getServer(), world, me.getRemotePlayerId(), sm.getSender()));
        canv.setPauseEnabled(false);
        p.setCanvas(canv);
        getFrontEnd().getHost().switchToPage(p);
        
        RemoteProxyWorldProtocol protocol = new RemoteProxyWorldProtocol(getServer(), world);
        getServer().setProtocol(protocol);
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        boolean received = true;
        switch(sm.getMessage().getType()){
            case WAITING_ROOM_INIT:
                receiveInit(sm);
                break;
            case WAITING_ROOM_UPDATE:
                receiveUpdate(sm);
                break;
            case REQUEST_PLAYER_DATA:
                receiveBuildRequest(sm);
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
}
