package orpheus.core.net;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

import orpheus.core.net.protocols.MessageHandler;

/**
 * This class represents either a client or server in a multiplayer Orpheus game.
 * 
 * @author Matt Crow
 */
public abstract class AbstractNetworkClient {

    /**
     * Formerly known as a "protocol".
     * Determines how to handle messages based on what "mode" this is in.
     * For example, it doesn't make sense to handle player control messages when not playing a game.
     */
    private volatile Optional<MessageHandler> messageHandler = Optional.empty();

    private Optional<Consumer<ChatMessage>> chatMessageHandler = Optional.empty();
    
    /**
     * messages are cached if this does not yet have a way of handling them
     */
    private LinkedList<Message> cachedMessages = new LinkedList<>();
    
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
        var messagesWeStillCannotHandle = new LinkedList<Message>();
        for (var message : cachedMessages) {
            var handled = unwrapped.handleMessage(message);
            if (!handled) {
                messagesWeStillCannotHandle.add(message);
            }
        }
        cachedMessages = messagesWeStillCannotHandle;
    }
    
    public void setChatMessageHandler(Consumer<ChatMessage> chatMessageHandler){
        this.chatMessageHandler = Optional.of(chatMessageHandler);
        var newCachedMessages = new LinkedList<Message>();
        cachedMessages.forEach((Message sm)->{
            if (sm.getType() == ServerMessageType.CHAT) {
                chatMessageHandler.accept(ChatMessage.fromJson(sm.getBody()));
            } else {
                newCachedMessages.add(sm);
            }
        });
        cachedMessages.clear();
        cachedMessages.addAll(newCachedMessages);
    }
    
    public final void receiveMessage(Connection connection, Message sm){
        doReceiveMessage(connection, sm);
        
        boolean handled = messageHandler
            .map(mh -> mh.handleMessage(sm))
            .orElse(false);
        
        if (sm.getType() == ServerMessageType.CHAT && chatMessageHandler.isPresent()){
            handled = true;
            chatMessageHandler.get().accept(ChatMessage.fromJson(sm.getBody()));
        }
        
        if(!handled){
            System.err.printf("Couldn't handle message with type %s\n", sm.getType().toString());
            cachedMessages.add(sm);
        }
    }
    
    
    protected abstract void doReceiveMessage(Connection connection, Message sm);
    public abstract void send(Message sm);
}
