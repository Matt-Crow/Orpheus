package net.protocols;

import java.util.HashSet;
import net.AbstractNetworkClient;
import orpheus.core.users.User;

/**
 *
 * @author Matt
 */
public abstract class AbstractWaitingRoomProtocol extends AbstractOrpheusServerNonChatProtocol{
    /*
    Keeps track of which Users want to play.
    */
    private final HashSet<User> teamProto;
    
    public AbstractWaitingRoomProtocol(AbstractNetworkClient runningServer){
        super(runningServer);
        teamProto = new HashSet<>();
    }
    
    public final boolean containsUser(User u){
        return teamProto.contains(u);
    }
    
    public final boolean addToTeamProto(User u){
        boolean shouldAdd = !containsUser(u);
        if(shouldAdd){
            teamProto.add(u);
        }
        return shouldAdd;
    }
    
    public final User[] getTeamProto(){
        return teamProto.toArray(new User[teamProto.size()]);
    }
}
