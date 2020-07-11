package net.protocols;

import controllers.User;
import java.math.BigDecimal;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;

/**
 *
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol implements AbstractOrpheusServerProtocol{
    
    /*
    measured in seconds
    */
    private static final int WAIT_TIME = 0;
    
    /*
    Keeps track of which Users want to play.
    The key is their IP address,
    while the value is the User
    */
    private final HashMap<String, User> teamProto;
    private final int numWaves;
    private final int maxEnemyLevel;
    
    public WaitingRoomHostProtocol(int enemyWaveCount, int maxEnemyLv){
        numWaves = enemyWaveCount;
        maxEnemyLevel = maxEnemyLv;
        teamProto = new HashMap<>();    
    }
    
    public final void resetTeamProto(){
        teamProto.clear();
    }
    
    private void receiveJoin(ServerMessage sm, OrpheusServer server){
        String joiningIp = sm.getIpAddr();
        
        // create the init message to bring the joining user up to 
        // date on the current waiting room status
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
