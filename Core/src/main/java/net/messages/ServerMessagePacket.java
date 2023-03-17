package net.messages;

import java.net.Socket;

import orpheus.core.net.messages.Message;
import orpheus.core.users.User;

/**
 * The ServerMessagePacket class is used to add information
 * to a serialized ServerMessage when it is received through
 * a Connection. It essentially adds a sender address "header"
 * to the message, and allows Connection to set LocalUser information
 * as well.
 * 
 * @author Matt Crow
 */
public class ServerMessagePacket {
    private final Socket sendingSocket;
    private User fromUser;
    private final Message containedMessage;
    /**
     * 
     * 
     * @param sendingSocket the Socket this message was received from
     * @param packetContents the message contained herein
     */
    public ServerMessagePacket(Socket sendingSocket, Message packetContents){
        this.sendingSocket = sendingSocket;
        containedMessage = packetContents;
    }
    
    public final Message getMessage(){
        return containedMessage;
    }
    
    public final Socket getSendingSocket(){
        return sendingSocket;
    }
    
    /**
     * Called by OrpheusServer to associate a logged-in LocalUser with this message.
     * @param u the user to associate this message with
     */
    public final void setSender(User u){
        fromUser = u;
    }
    public final User getSender(){
        return fromUser;
    }
    
    @Override
    public String toString(){
        return String.format(
            "ServerMessagePacket from %s:%d (%s):\n%s\nEND OF MESSAGE", 
            sendingSocket.getInetAddress().toString(), 
            sendingSocket.getPort(), 
            (fromUser == null) ? "Unknown LocalUser" : fromUser.getName(), 
            containedMessage.toString()
        );
    }
}
