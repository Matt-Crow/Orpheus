package net.protocols;

import java.util.HashMap;
import users.AbstractUser;
import gui.pages.worldSelect.AbstractWaitingRoom;
import java.net.InetAddress;

/**
 *
 * @author Matt
 */
public abstract class AbstractWaitingRoomProtocol extends AbstractOrpheusServerNonChatProtocol{
    private final AbstractWaitingRoom frontEnd;
    /*
    Keeps track of which Users want to play.
    The key is their IP address,
    while the value is the User
    */
    private final HashMap<InetAddress, AbstractUser> teamProto;
    
    public AbstractWaitingRoomProtocol(AbstractWaitingRoom linkedRoom){
        frontEnd = linkedRoom;
        teamProto = new HashMap<>();
    }
    
    public final void resetTeamProto(){
        teamProto.clear();
    }
    
    public final boolean containsIp(InetAddress ipAddr){
        return teamProto.containsKey(ipAddr);
    }
    public final boolean containsUser(AbstractUser u){
        return containsIp(u.getIpAddress());
    }
    
    public final boolean addToTeamProto(AbstractUser u){
        boolean shouldAdd = !containsUser(u);
        if(shouldAdd){
            teamProto.put(u.getIpAddress(), u);
            frontEnd.updateTeamDisplays();
        }
        return shouldAdd;
    }
    
    public final AbstractUser[] getTeamProto(){
        return teamProto.values().stream().toArray(size -> new AbstractUser[size]);
    }
    
    public final AbstractWaitingRoom getFrontEnd(){
        return frontEnd;
    }
}
