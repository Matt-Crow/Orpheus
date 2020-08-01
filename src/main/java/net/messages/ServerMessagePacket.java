package net.messages;

import java.io.StringReader;
import java.net.InetAddress;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import serialization.JsonUtil;
import users.AbstractUser;

/**
 * The ServerMessagePacket class is used to send information back
 and forth between computers.
 * @author Matt Crow
 */
public class ServerMessagePacket extends ServerMessage {
    
    private final InetAddress ipAddr;
    private AbstractUser fromUser;
    
    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param ipAddress the IP address this was sent from
     * @param bodyText the text of the message
     * @param messageType an enum value representing the type of message this is.
     * the receiving servers will react to this message based upon this type
     */
    public ServerMessagePacket(InetAddress ipAddress, String bodyText, ServerMessageType messageType){
        super(bodyText, messageType);
        ipAddr = ipAddress;
    }
    
    public final InetAddress getSendingIp(){
        return ipAddr;
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
    
    /**
     * Attempts to convert a string to a JsonObject,
 then attempts to convert that JsonObject to a ServerMessagePacket
     * @param from the InetAddress of the socket this message was
     * received from.
     * 
     * @param s the string to deserialize
     * @return
     * @throws JsonException 
     */
    public static ServerMessagePacket deserializeJson(InetAddress from, String s) throws JsonException{
        JsonObject obj = Json.createReader(new StringReader(s)).readObject();
        JsonUtil.verify(obj, "type");
        JsonUtil.verify(obj, "body");
        return new ServerMessagePacket(
            from,
            obj.getString("body"),
            ServerMessageType.fromString(obj.getString("type"))
        );
    }
    
    @Override
    public String toString(){
        return String.format("ServerMessagePacket from %s (%s):\n%s\nEND OF MESSAGE", ipAddr.toString(), (fromUser == null) ? "Unknown User" : fromUser.getName(), super.toString());
    }
}
