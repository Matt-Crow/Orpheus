package start;

import users.AbstractUser;
import commands.OrpheusCommand;

/**
 * The AbstractOrpheusClient provides a layer of abstraction to how the program
 * reacts based on whether the user is playing a multi-player or single-player
 * game
 * 
 * @author Matt Crow
 */
public abstract class AbstractOrpheusClient {
    private final AbstractUser user;
    
    protected AbstractOrpheusClient(AbstractUser user){
        this.user = user;
    }
    
    public final AbstractUser getUser(){
        return user;
    }
    
    public final void execute(OrpheusCommand cmd){
        doSendMessage(cmd);
    }
    
    public final void receiveMessage(OrpheusCommand packet){
        doReceiveMessage(packet);
    }
    
    public abstract void doSendMessage(OrpheusCommand packet);
    public abstract void doReceiveMessage(OrpheusCommand packet);
}
