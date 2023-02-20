package orpheus.core.net.chat;

import orpheus.core.users.User;

/**
 * designates a class as one which handles chat messages received
 */
public interface ChatProtocol {
    
    /**
     * Called whenever the server receives a chat message.
     * @param sender the user who sent the message
     * @param message the message the user sent
     */
    public void receiveChatMessage(User sender, String message);
}
