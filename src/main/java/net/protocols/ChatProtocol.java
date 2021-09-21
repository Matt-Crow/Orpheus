package net.protocols;

import gui.components.Chat;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;

/**
 * @author Matt Crow
 */
public class ChatProtocol extends AbstractOrpheusServerProtocol{
    private final Chat widget; // he he he. Widgit is fun to say
    
    public ChatProtocol(OrpheusServer runningServer, Chat chat){
        super(runningServer);
        widget = chat;
    }
    
    private void receiveChatMsg(ServerMessagePacket sm){
        widget.logLocal(String.format("(%s): %s", sm.getSender().getName(), sm.getMessage().getBody()));
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
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
