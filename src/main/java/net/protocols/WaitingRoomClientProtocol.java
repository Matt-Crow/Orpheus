package net.protocols;

import controllers.Master;
import controllers.User;
import controls.RemotePlayerControls;
import customizables.BuildJsonUtil;
import java.io.IOException;
import javax.json.JsonObject;
import javax.json.JsonValue;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import serialization.JsonUtil;
import windows.WorldSelect.AbstractWaitingRoom;
import windows.world.WorldCanvas;
import windows.world.WorldPage;
import world.RemoteProxyWorld;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class WaitingRoomClientProtocol extends AbstractWaitingRoomProtocol{
    private final String hostIp;
    public WaitingRoomClientProtocol(AbstractWaitingRoom linkedRoom, String hostIpAddr) {
        super(linkedRoom);
        hostIp = hostIpAddr;
    }
    
    @Override
    public void doApplyProtocol() {
        resetTeamProto();
        getFrontEnd().getChat().openChatServer();
        getFrontEnd().getChat().joinChat(hostIp);
        getFrontEnd().getChat().logLocal("Connected to host " + hostIp);
    }
    
    private void receiveInit(ServerMessage sm){
        resetTeamProto();
        JsonObject obj = JsonUtil.fromString(sm.getBody());
        JsonUtil.verify(obj, "team");
        obj.getJsonArray("team").stream().forEach((jv)->{
            if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                addToTeamProto(User.deserializeJson((JsonObject)jv));
            }
        });
    }
    
    private void receiveUpdate(ServerMessage sm){
        addToTeamProto(sm.getSender());
    }
    
    private synchronized void receiveBuildRequest(ServerMessage sm){
        OrpheusServer.getInstance().send(new ServerMessage(
            BuildJsonUtil.serializeJson(getFrontEnd().getSelectedBuild()).toString(),
            ServerMessageType.PLAYER_DATA
        ), sm.getIpAddr());
        getFrontEnd().setInputEnabled(false);
    }
    
    private void receiveRemoteId(ServerMessage sm){
        Master.getUser().setRemotePlayerId(sm.getBody());
    }
    
    /**
     * allows remote users to receive and de-serialize the AbstractWorld created by the host.
     * 
     * this method is currently having problems, as the enemy team might not serialize,
     * and it takes a couple seconds to load teams into the world
     * 
     * @param sm 
     */
    private void receiveWorldInit(ServerMessage sm){
        WorldContent w = WorldContent.fromSerializedString(sm.getBody());
        RemoteProxyWorld world = new RemoteProxyWorld(sm.getIpAddr(), w);
        
        User me = Master.getUser();
        me.linkToRemotePlayerInWorld(world);
        world.createCanvas();
        w.init(); // do I need this?
        
        WorldPage p = new WorldPage();
        WorldCanvas canv = world.getCanvas();
        canv.addPlayerControls(new RemotePlayerControls(me.getPlayer(), world, sm.getIpAddr()));
        canv.setPauseEnabled(false);
        p.setCanvas(canv);
        getFrontEnd().getHost().switchToPage(p);
        
        try {
            new RemoteProxyProtocol(world).applyProtocol();
        } catch (IOException ex) {
            System.err.println("Failed to apply RemoteProxyProtocol");
            ex.printStackTrace();
        }
    }
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean received = true;
        switch(sm.getType()){
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
