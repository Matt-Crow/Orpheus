package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.users.User;

/**
 * Broadcasts chat messages received to all clients
 */
public class ServerChatProtocol implements ChatProtocol {

    private final OrpheusServer runningOn;

    public ServerChatProtocol(OrpheusServer runningOn) {
        this.runningOn = runningOn;
    }

    @Override
    public void receiveChatMessage(User sender, String message) {
        var formatted = String.format("(%s): %s", sender.getName(), message);
        var sendMe = new ServerMessage(formatted, ServerMessageType.CHAT);
        runningOn.send(sendMe); // todo don't send to sender
    }
}
