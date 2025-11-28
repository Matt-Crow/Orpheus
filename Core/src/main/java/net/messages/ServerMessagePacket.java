package net.messages;

import orpheus.core.net.messages.Message;

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
    private final Message containedMessage;
    /**
     * 
     * 
     * @param sendingSocket the Socket this message was received from
     * @param packetContents the message contained herein
     */
    public ServerMessagePacket(Message packetContents){
        containedMessage = packetContents;
    }
    
    public final Message getMessage(){
        return containedMessage;
    }

    @Override
    public String toString(){
        return containedMessage.toString();
    }
}
