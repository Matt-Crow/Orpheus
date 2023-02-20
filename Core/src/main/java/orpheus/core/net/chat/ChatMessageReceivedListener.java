package orpheus.core.net.chat;

/**
 * notified whenever a client receives a chat message
 */
public interface ChatMessageReceivedListener {
    
    /**
     * Called whenever the client receives a chat message from the server.
     * @param message the message received.
     */
    public void chatMessageReceived(ChatMessage message);
}
