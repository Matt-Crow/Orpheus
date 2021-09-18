package net.protocols;

import users.AbstractUser;
import gui.pages.worldSelect.AbstractWaitingRoom;
import java.util.HashSet;
import net.OrpheusServer;

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
    private final HashSet<AbstractUser> teamProto;
    
    public AbstractWaitingRoomProtocol(OrpheusServer runningServer, AbstractWaitingRoom linkedRoom){
        super(runningServer);
        frontEnd = linkedRoom;
        teamProto = new HashSet<>();
    }
    
    public final void resetTeamProto(){
        teamProto.clear();
    }
    
    public final boolean containsUser(AbstractUser u){
        return teamProto.contains(u);
    }
    
    public final boolean addToTeamProto(AbstractUser u){
        boolean shouldAdd = !containsUser(u);
        if(shouldAdd){
            teamProto.add(u);
            frontEnd.updateTeamDisplays();
        }
        return shouldAdd;
    }
    
    public final AbstractUser[] getTeamProto(){
        return teamProto.toArray(new AbstractUser[teamProto.size()]);
    }
    
    public final AbstractWaitingRoom getFrontEnd(){
        return frontEnd;
    }
}
