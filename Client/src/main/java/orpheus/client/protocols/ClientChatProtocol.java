package orpheus.client.protocols;

import net.messages.ServerMessageType;
import orpheus.client.gui.components.ChatBox;
import orpheus.core.net.chat.ChatMessage;
import orpheus.core.net.chat.ChatMessageReceivedListener;
import orpheus.core.net.chat.ChatMessageSentListener;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;

/**
 * Sends messages the user enters in a ChatBox, and uses that ChatBox to display
 * messages received.
 */
public class ClientChatProtocol implements ChatProtocol, ChatMessageSentListener, ChatMessageReceivedListener {

    /**
     * the user who sends messages through this protocol
     */
    private final User sender;

    /**
     * the client this is running on
     */
    private final net.OrpheusClient client;

    /**
     * the chat box which will send messages and display them
     */
    private final ChatBox chatBox;

    public ClientChatProtocol(User sender, net.OrpheusClient client, ChatBox chatBox) {
        this.sender = sender;
        this.client = client;
        this.chatBox = chatBox;
        chatBox.addMessageSentListener(this);
    }

    @Override
    public void chatMessageSent(String message) {
        client.send(new Message(
            ServerMessageType.CHAT, 
            new ChatMessage(sender, message).serializeJson())
        );
    }

    @Override
    public void chatMessageReceived(ChatMessage message) {
        chatBox.output(String.format("(%s): %s", message.getSender().getName(), message.getMessage()));
    }

    @Override
    public void receiveChatMessage(ChatMessage message) {
        chatMessageReceived(message);    
    }
}
