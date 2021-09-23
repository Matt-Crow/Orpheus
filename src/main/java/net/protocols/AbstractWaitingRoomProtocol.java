package net.protocols;

import users.AbstractUser;
import gui.pages.worldSelect.WaitingRoom;
import java.util.HashSet;
import net.AbstractNetworkClient;
import net.OrpheusServer;

/**
 *
 * @author Matt
 */
public abstract class AbstractWaitingRoomProtocol<T extends AbstractNetworkClient> extends AbstractOrpheusServerNonChatProtocol<T>{
    /*
    Keeps track of which Users want to play.
    */
    private final HashSet<AbstractUser> teamProto;
    
    public AbstractWaitingRoomProtocol(T runningServer){
        super(runningServer);
        teamProto = new HashSet<>();
    }
    
    public final boolean containsUser(AbstractUser u){
        return teamProto.contains(u);
    }
    
    public final boolean addToTeamProto(AbstractUser u){
        boolean shouldAdd = !containsUser(u);
        if(shouldAdd){
            teamProto.add(u);
        }
        return shouldAdd;
    }
    
    public final AbstractUser[] getTeamProto(){
        return teamProto.toArray(new AbstractUser[teamProto.size()]);
    }
}
