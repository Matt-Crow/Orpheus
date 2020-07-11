package net.protocols;

import controllers.User;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import windows.WorldSelect.HostWaitingRoom;

/**
 *
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol extends AbstractWaitingRoomProtocol{    
    private final int numWaves;
    private final int maxEnemyLevel;
    
    public WaitingRoomHostProtocol(HostWaitingRoom host, OrpheusServer server, int enemyWaveCount, int maxEnemyLv){
        super(host, server);
        numWaves = enemyWaveCount;
        maxEnemyLevel = maxEnemyLv;    
    }
    
    /**
     * Puts the given user on the teamProto,
     * and alerts all connected players
     * @param u 
     */
    public final void addUserToTeam(User u){
        if(addToTeamProto(u)){
            ServerMessage sm = new ServerMessage(
                "join player team",
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            getServer().send(sm);
        }
    }
    
    /**
     * Called whenever a new player connects to the server.
     * Adds them to the player team, 
     * notifies the other players,
     * and sends the new player the current waiting room status.
     * 
     * @param sm
     * @param server 
     */
    private void receiveJoin(ServerMessage sm){
        String joiningIp = sm.getIpAddr();
        if(containsIp(joiningIp)){
            return;
        } // ##################### RETURNS HERE IF ALREADY JOINED
        
        /*
        First, add the new player to the player team.
        addUserToTeam automatically notifies everone 
        connected that someone has joined
        */
        User joiningUser = sm.getSender();
        addUserToTeam(joiningUser);
        
        /*
        Second, create the init message to bring 
        the joining user up to date on the current 
        waiting room status
        */
        JsonObjectBuilder initMsgBuild = Json.createObjectBuilder();
        initMsgBuild.add("type", "waiting room init");
        JsonArrayBuilder userListBuild = Json.createArrayBuilder();
        for(User u : getTeamProto()){
            userListBuild.add(u.serializeJson());
        }
        initMsgBuild.add("team", userListBuild.build());
        
        ServerMessage initMsg = new ServerMessage(
            initMsgBuild.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        
        getServer().send(initMsg, joiningIp);
    }
    
    public final void prepareToStart(){
        WaitingRoomHostBuildProtocol protocol = new WaitingRoomHostBuildProtocol(
            (HostWaitingRoom)getFrontEnd(),
            getServer(),
            getTeamProto(),
            numWaves,
            maxEnemyLevel
        );
        getServer().setProtocol(protocol);
        protocol.start();
    }
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean handled = true;
        
        switch(sm.getType()){
            case PLAYER_JOINED:
                receiveJoin(sm);
                break;
            default:
                handled = false;
                break;
        }
        
        return handled;
    }

}
