package net.protocols;

import controllers.User;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import windows.WorldSelect.WSWaitForPlayers;

/**
 *
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol implements AbstractOrpheusServerProtocol{
    
    /*
    measured in seconds
    */
    private static final int WAIT_TIME = 0;
    
    private final WSWaitForPlayers frontEnd;
    /*
    Keeps track of which Users want to play.
    The key is their IP address,
    while the value is the User
    */
    private final HashMap<String, User> teamProto;
    private final int numWaves;
    private final int maxEnemyLevel;
    
    public WaitingRoomHostProtocol(WSWaitForPlayers host, int enemyWaveCount, int maxEnemyLv){
        frontEnd = host;
        numWaves = enemyWaveCount;
        maxEnemyLevel = maxEnemyLv;
        teamProto = new HashMap<>();    
    }
    
    public final void resetTeamProto(){
        teamProto.clear();
    }
    
    /**
     * Puts the given user on the teamProto,
     * and alerts all connected players
     * @param u 
     * @param server 
     */
    public final void addUserToTeam(User u, OrpheusServer server){
        if(!teamProto.containsKey(u.getIpAddress())){
            // hasn't joined yet
            teamProto.put(u.getIpAddress(), u);
            ServerMessage sm = new ServerMessage(
                "join player team",
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            server.send(sm);
            frontEnd.updateTeamDisplays();
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
    private void receiveJoin(ServerMessage sm, OrpheusServer server){
        String joiningIp = sm.getIpAddr();
        if(teamProto.containsKey(joiningIp)){
            return;
        } // ##################### RETURNS HERE IF ALREADY JOINED
        
        /*
        First, add the new player to the player team.
        addUserToTeam automatically notifies everone 
        connected that someone has joined
        */
        User joiningUser = sm.getSender();
        addUserToTeam(joiningUser, server);
        
        /*
        Second, create the init message to bring 
        the joining user up to date on the current 
        waiting room status
        */
        JsonObjectBuilder initMsgBuild = Json.createObjectBuilder();
        initMsgBuild.add("type", "waiting room init");
        JsonArrayBuilder userListBuild = Json.createArrayBuilder();
        teamProto.values().stream().forEach((User u)->{
            userListBuild.add(u.serializeJson());
        });
        initMsgBuild.add("team", userListBuild.build());
        
        ServerMessage initMsg = new ServerMessage(
            initMsgBuild.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        
        server.send(initMsg, joiningIp);
    }
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean handled = true;
        
        switch(sm.getType()){
            case PLAYER_JOINED:
                receiveJoin(sm, forServer);
                break;
            default:
                handled = false;
                break;
        }
        
        return handled;
    }

}
