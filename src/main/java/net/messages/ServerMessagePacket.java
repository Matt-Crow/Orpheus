package net.messages;

import java.net.InetAddress;
import users.AbstractUser;

/**
 * The ServerMessagePacket class is used to add information
 * to a serialized ServerMessage when it is received through
 * a Connection. It essentially adds a sender address "header"
 * to the message, and allows Connection to set User information
 * as well.
 * 
 * @author Matt Crow
 */
public class ServerMessagePacket {
    private final InetAddress sendingIpAddress;
    private AbstractUser fromUser;
    private final ServerMessage containedMessage;
    /**
     * 
     * 
     * @param ipAddress the IP address this was sent from
     * @param packetContents the message contained herein
     */
    public ServerMessagePacket(InetAddress ipAddress, ServerMessage packetContents){
        sendingIpAddress = ipAddress;
        containedMessage = packetContents;
    }
    
    public final ServerMessage getMessage(){
        return containedMessage;
    }
    
    public final InetAddress getSendingIp(){
        return sendingIpAddress;
    }
    
    /**
     * Called by OrpheusServer to associate
     * a logged-in User with this message.
     * @param u the user to associate this 
     * message with
     */
    public final void setSender(AbstractUser u){
        fromUser = u;
    }
    public final AbstractUser getSender(){
        return fromUser;
    }
    
    @Override
    public String toString(){
        return String.format("ServerMessagePacket from %s (%s):\n%s\nEND OF MESSAGE", sendingIpAddress.toString(), (fromUser == null) ? "Unknown User" : fromUser.getName(), containedMessage.toString());
    }
}
