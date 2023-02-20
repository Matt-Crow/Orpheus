package orpheus.client.protocols;

import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import orpheus.client.gui.components.ChatBox;
import orpheus.core.net.ClientProtocol;
import orpheus.core.net.chat.ChatMessageReceivedListener;
import orpheus.core.net.chat.ChatMessageSentListener;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.net.connections.Connection;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;

/**
 * Sends messages the user enters in a ChatBox, and uses that ChatBox to display
 * messages received.
 */
public class ClientChatProtocol implements ClientProtocol, ChatProtocol, ChatMessageSentListener, ChatMessageReceivedListener {

    /**
     * the client this is running on
     */
    private final net.OrpheusClient client;

    /**
     * the chat box which will send messages and display them
     */
    private final ChatBox chatBox;

    public ClientChatProtocol(net.OrpheusClient client, ChatBox chatBox) {
        this.client = client;
        this.chatBox = chatBox;
        chatBox.addMessageSentListener(this);
    }

    @Override
    public void receive(orpheus.core.net.OrpheusClient client, Connection from, Message message) {
        throw new UnsupportedOperationException("Unimplemented method 'receive'");
    }

    @Override
    public void chatMessageSent(String message) {
        client.send(new ServerMessage(message, ServerMessageType.CHAT));
    }

    @Override
    public void chatMessageReceived(String message) {
        chatBox.output(message); // todo may need sender
    }

    @Override
    public void receiveChatMessage(User sender, String message) {
        chatMessageReceived(String.format("(%s): %s", "sender.getName()", message));    
    }
}
