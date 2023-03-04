package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessageType;
import orpheus.core.net.chat.ChatMessage;
import orpheus.core.net.chat.ChatProtocol;
import orpheus.core.net.messages.Message;

/**
 * Broadcasts chat messages received to all clients
 */
public class ServerChatProtocol implements ChatProtocol {

    private final OrpheusServer runningOn;

    public ServerChatProtocol(OrpheusServer runningOn) {
        this.runningOn = runningOn;
    }

    @Override
    public void receiveChatMessage(ChatMessage message) {
        var sendMe = new Message(ServerMessageType.CHAT, message.toJson());
        runningOn.sendToAllExcept(sendMe, message.getSender());
    }
}
