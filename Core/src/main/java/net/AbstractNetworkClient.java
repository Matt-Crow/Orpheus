package net;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.AbstractProtocol;
import orpheus.core.net.chat.ChatMessage;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.net.messages.Message;

/**
 * This class represents either a client or server in a multiplayer Orpheus game.
 * 
 * @author Matt Crow
 */
public abstract class AbstractNetworkClient {
    private volatile boolean isStarted;
    private volatile AbstractProtocol protocol;

    /**
     * handles chat messages received
     */
    private Optional<ChatProtocol> chatProtocol;
    
    /**
     * messages are cached if this does not yet have a way of handling them
     */
    private LinkedList<ServerMessagePacket> cachedMessages;
    
    public AbstractNetworkClient(){
        isStarted = false;
        protocol = null;
        chatProtocol = Optional.empty();
        cachedMessages = new LinkedList<>();
    }
    
    
    public final boolean isStarted(){
        return isStarted;
    }
    
    public final void setProtocol(AbstractProtocol protocol){
        this.protocol = protocol;
        LinkedList<ServerMessagePacket> smps = new LinkedList<>();
        boolean wasHandled;
        for (ServerMessagePacket smp : cachedMessages) {
            wasHandled = protocol.receive(smp);
            if (!wasHandled) {
                // add back to queue if not received
                smps.add(smp);
            }
        }
        cachedMessages = smps;
    }
    
    /**
     * Sets the separate protocol to receive chat 
     * messages. This way, I can't have multiple
     * regular protocols, but chat also can't interfere
     * with the other protocol!
     * 
     * @param chatProtocol the ChatProtocol to handle chat messages
     * received by this server.
     */
    public void setChatProtocol(ChatProtocol chatProtocol){
        this.chatProtocol = Optional.of(chatProtocol);
        var newCachedMessages = new LinkedList<ServerMessagePacket>();
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if (sm.getMessage().getType() == ServerMessageType.CHAT) {
                chatProtocol.receiveChatMessage(ChatMessage.fromJson(sm.getMessage().getBody()));
            } else {
                newCachedMessages.add(sm);
            }
        });
        cachedMessages.clear();
        cachedMessages.addAll(newCachedMessages);
    }
    
    /*
    Don't call this in start, in case the protocol is set before starting the
    server
    */
    public final void clearProtocols(){
        protocol = null;
        //chatProtocol = null; don't do this!
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
            handled = protocol.receive(sm);
        }
        if (chatProtocol.isPresent() && sm.getMessage().getType() == ServerMessageType.CHAT){
            handled = true;
            chatProtocol.get().receiveChatMessage(ChatMessage.fromJson(sm.getMessage().getBody()));
        }
        
        if(!handled){
            System.err.printf("Couldn't handle message with type %s\n", sm.getMessage().getType().toString());
            cachedMessages.add(sm);
        }
    }
    
    
    protected abstract void doStart() throws IOException;
    protected abstract void doStop() throws IOException;
    protected abstract void doReceiveMessage(ServerMessagePacket sm);
    public abstract void send(Message sm);
}
