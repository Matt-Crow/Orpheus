package orpheus.core.net.chat;

/**
 * notified whenever a client sends a chat message
 */
public interface ChatMessageSentListener {
    
    /**
     * Called whenever the client sends a chat message.
     * @param message the chat message sent.
     */
    public void chatMessageSent(String message);
}
