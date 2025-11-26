package net;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.MessageHandler;
import orpheus.core.net.chat.ChatMessage;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.net.messages.Message;

/**
 * This class represents either a client or server in a multiplayer Orpheus game.
 * 
 * @author Matt Crow
 */
public abstract class AbstractNetworkClient {
    private volatile boolean isStarted = false;

    /**
     * Formerly known as a "protocol".
     * Determines how to handle messages based on what "mode" this is in.
     * For example, it doesn't make sense to handle player control messages when not playing a game.
     */
    private volatile Optional<MessageHandler> messageHandler = Optional.empty();

    /**
     * handles chat messages received
     */
    private Optional<ChatProtocol> chatProtocol = Optional.empty();
    
    /**
     * messages are cached if this does not yet have a way of handling them
     */
    private LinkedList<ServerMessagePacket> cachedMessages = new LinkedList<>();
    
    
    public final boolean isStarted(){
        return isStarted;
    }
    
    /**
     * Sets the new strategy for handling non-chat messages.
     * @param messageHandler the new strategy for handling messages
     */
    public final void setMessageHandler(Optional<MessageHandler> messageHandler) {
        this.messageHandler.ifPresent(MessageHandler::handleStop);

        this.messageHandler = messageHandler;

        if (messageHandler.isEmpty()) {
            return;
        }

        var unwrapped = messageHandler.get();
        unwrapped.handleStart();

        /**
         * We may have received some messages before we could set the protocol to handle them.
         * Look through our cached messages to see if this new strategy could have handled them.
         */
        var messagesWeStillCannotHandle = new LinkedList<ServerMessagePacket>();
        for (var message : cachedMessages) {
            var handled = unwrapped.handleMessage(message);
            if (!handled) {
                messagesWeStillCannotHandle.add(message);
            }
        }
        cachedMessages = messagesWeStillCannotHandle;
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
        
        boolean handled = messageHandler
            .map(mh -> mh.handleMessage(sm))
            .orElse(false);
        
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
