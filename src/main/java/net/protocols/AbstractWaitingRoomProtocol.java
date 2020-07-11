package net.protocols;

import controllers.User;
import java.util.HashMap;
import net.OrpheusServer;
import windows.WorldSelect.AbstractWaitingRoom;

/**
 *
 * @author Matt
 */
public abstract class AbstractWaitingRoomProtocol implements AbstractOrpheusServerProtocol{
    private final AbstractWaitingRoom frontEnd;
    /*
    Keeps track of which Users want to play.
    The key is their IP address,
    while the value is the User
    */
    private final HashMap<String, User> teamProto; 
    private final OrpheusServer server;
    
    public AbstractWaitingRoomProtocol(AbstractWaitingRoom linkedRoom, OrpheusServer forServer){
        frontEnd = linkedRoom;
        teamProto = new HashMap<>();
        server = forServer;
    }
    
    public final void resetTeamProto(){
        teamProto.clear();
    }
    
    public final boolean containsIp(String ipAddr){
        return teamProto.containsKey(ipAddr);
    }
    public final boolean containsUser(User u){
        return containsIp(u.getIpAddress());
    }
    
    public final boolean addToTeamProto(User u){
        boolean shouldAdd = !containsUser(u);
        if(shouldAdd){
            teamProto.put(u.getIpAddress(), u);
            frontEnd.updateTeamDisplays();
        }
        return shouldAdd;
    }
    
    public final User[] getTeamProto(){
        return teamProto.values().stream().toArray(size -> new User[size]);
    }
    
    public final AbstractWaitingRoom getFrontEnd(){
        return frontEnd;
    }
    
    public final OrpheusServer getServer(){
        return server;
    }
}
