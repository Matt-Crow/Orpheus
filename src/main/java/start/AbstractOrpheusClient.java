package start;

import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import users.AbstractUser;

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
    
    public final void sendMessage(ServerMessage sm){
        ServerMessagePacket packet = new ServerMessagePacket(null, sm);
        packet.setSender(user);
        doSendMessage(packet);
    }
    
    public final void receiveMessage(ServerMessagePacket packet){
        doReceiveMessage(packet);
    }
    
    public abstract void doSendMessage(ServerMessagePacket packet);
    public abstract void doReceiveMessage(ServerMessagePacket packet);
}
