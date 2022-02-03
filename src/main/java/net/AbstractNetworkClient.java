package net;

import java.io.IOException;
import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import net.protocols.AbstractOrpheusServerNonChatProtocol;
import net.protocols.ChatProtocol;
import util.SafeList;

/**
 * This class represents either a client or server in a multiplayer Orpheus game.
 * 
 * @author Matt Crow
 */
public abstract class AbstractNetworkClient {
    private volatile boolean isStarted;
    private volatile AbstractOrpheusServerNonChatProtocol protocol;
    private volatile ChatProtocol<? super AbstractNetworkClient> chatProtocol;
    
    //messages are cached if this does not yet have a way of handling them
    private final SafeList<ServerMessagePacket> cachedMessages;
    
    public AbstractNetworkClient(){
        isStarted = false;
        protocol = null;
        chatProtocol = null;
        cachedMessages = new SafeList<>();
    }
    
    
    
    public final boolean isStarted(){
        return isStarted;
    }
    
    
    
    public final void setProtocol(AbstractOrpheusServerNonChatProtocol protocol){
        this.protocol = protocol;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(protocol.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
                System.out.println("uncached message " + sm.hashCode());
            }
        });
    }
    
    /**
     * Sets the separate protocol to receive chat 
     * messages. This way, I can't have multiple
     * regular protocols, but chat also can't interfere
     * with the other protocol!
     * 
     * ... or I could just have the other protocols
     * forward to their chat...
     * 
     * @param chatProtocol the ChatProtocol to handle messages
     * received by this server.
     */
    public final void setChatProtocol(ChatProtocol<? super AbstractNetworkClient> chatProtocol){
        this.chatProtocol = chatProtocol;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(chatProtocol.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
            }
        });
    }
    
    /*
    Don't call this in start, in case the protocol is set before starting the
    server
    */
    public final void clearProtocols(){
        protocol = null;
        chatProtocol = null;
    }
    
    public final void start() throws IOException {
        if(!isStarted){
            doStart();
            isStarted = true;
        }
    }
    
    public final void stop() throws IOException {
        if(isStarted){
            doStop();
            isStarted = false;
        }
    }
    
    public final void receiveMessage(ServerMessagePacket sm){
        doReceiveMessage(sm);
        
        boolean handled = false;
        if(protocol != null){
            handled = protocol.receiveMessage(sm, this);
        }
        if(chatProtocol != null){
            // old handled value AFTER chatProtocol, lest short-circuit
            handled = chatProtocol.receiveMessage(sm, this) || handled;
        }
        
        if(!handled){
            System.err.printf("Couldn't handle %s\n", sm.getMessage().getType().toString());
            cachedMessages.add(sm);
        }
    }
    
    
    protected abstract void doStart() throws IOException;
    protected abstract void doStop() throws IOException;
    protected abstract void doReceiveMessage(ServerMessagePacket sm);
    protected abstract void send(ServerMessage sm);
}
