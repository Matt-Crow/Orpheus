package net.protocols;

import java.util.Collection;
import java.util.LinkedList;
import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;

/**
 * @author Matt Crow
 */
public class ChatProtocol extends AbstractProtocol {
    private final Collection<ChatMessageListener> chatListeners;
    
    
    public ChatProtocol(AbstractNetworkClient runningServer){
        super(runningServer);
        chatListeners = new LinkedList<>();
    }
    
    
    /**
     * adds the given chat listener to this, if it is not already listening to 
     * this
     * 
     * @param cml will receive chat messages received by this
     * @return true if the listener was not already listinging to this
     */
    public boolean addChatListener(ChatMessageListener cml){
        boolean alreadyAdded = hasChatListener(cml);
        if(!alreadyAdded){
            chatListeners.add(cml);
        }
        return !alreadyAdded;
    }
    
    public boolean hasChatListener(ChatMessageListener cml){
        return chatListeners.contains(cml);
    }
    
    /**
     * removes the given lister from this, if it is already listening to this.
     * 
     * @param cml will be removed from this
     * @return false if the listener was not originally listening to this 
     */
    public boolean removeChatListener(ChatMessageListener cml){
        boolean ableToRemove = hasChatListener(cml);
        if(ableToRemove){
            chatListeners.remove(cml);
        }
        return ableToRemove;
    }
    
    private void receiveChatMsg(ServerMessagePacket sm){
        String msg = String.format("(%s): %s", sm.getSender().getName(), sm.getMessage().getBody());
        chatListeners.forEach(cml -> cml.messageReceived(msg));
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
