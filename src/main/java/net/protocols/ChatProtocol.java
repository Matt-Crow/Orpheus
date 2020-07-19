package net.protocols;

import gui.Chat;
import java.io.IOException;
import net.OrpheusServer;
import net.ServerMessage;

/**
 * need to override apply protocol so this doesn't apply over other protocols
 * @author Matt
 */
public class ChatProtocol extends AbstractOrpheusServerProtocol{
    private final Chat widget; // he he he. Widgit is fun to say
    
    public ChatProtocol(Chat chat){
        widget = chat;
    }
    
    @Override
    public void applyProtocol() throws IOException {
        OrpheusServer server = OrpheusServer.getInstance();
        server.restart();
        server.setChatProtocol(this);
        doApplyProtocol();
    }

    @Override
    public void doApplyProtocol() {
        String ip = OrpheusServer.getInstance().getIpAddr();
        widget.logLocal("Initialized chat server on " + ip);
        widget.logLocal("Have other people use the \'/connect " + ip + "\' command (without the quote marks) to connect.");
    }
    
    private void receiveChatMsg(ServerMessage sm){
        widget.logLocal(String.format("(%s): %s", sm.getSender().getName(), sm.getBody()));
    }
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean handled = true;
        switch(sm.getType()){
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
