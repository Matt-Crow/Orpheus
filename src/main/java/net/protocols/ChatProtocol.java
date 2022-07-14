package net.protocols;

import gui.components.Chat;
import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;

/**
 * @author Matt Crow
 */
public class ChatProtocol extends AbstractProtocol {
    private final Chat widget; // he he he. Widgit is fun to say
    
    public ChatProtocol(AbstractNetworkClient runningServer, Chat chat){
        super(runningServer);
        widget = chat;
    }
    
    private void receiveChatMsg(ServerMessagePacket sm){
        widget.logLocal(String.format("(%s): %s", sm.getSender().getName(), sm.getMessage().getBody()));
    }
    
    @Override
    public boolean receive(ServerMessagePacket sm) {
        boolean handled = true;
        switch(sm.getMessage().getType()){
            case CHAT:
                receiveChatMsg(sm);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
}
