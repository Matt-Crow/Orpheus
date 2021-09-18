package net.protocols;

import gui.components.Chat;
import java.io.IOException;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;

/**
 * need to override apply protocol so this doesn't apply over other protocols
 * @author Matt
 */
public class ChatProtocol extends AbstractOrpheusServerProtocol{
    private final Chat widget; // he he he. Widgit is fun to say
    
    public ChatProtocol(OrpheusServer runningServer, Chat chat){
        super(runningServer);
        widget = chat;
    }
    
    @Override
    public void applyProtocol() throws IOException {
        OrpheusServer server = getServer();
        server.restart();
        server.setChatProtocol(this);
        doApplyProtocol();
    }

    @Override
    public void doApplyProtocol() {
        String conn = getServer().getConnectionString();
        widget.logLocal(String.format("Have other people use the /connect %s command to connect.", conn));
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
