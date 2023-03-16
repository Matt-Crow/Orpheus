package orpheus.core.net.chat;

/**
 * designates a class as one which handles chat messages received
 */
public interface ChatProtocol {
    
    /**
     * @param message the message received
     */
    public void receiveChatMessage(ChatMessage message);
}
