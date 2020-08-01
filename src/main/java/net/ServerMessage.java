package net;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;

/**
 * The ServerMessage class is used to send information back
 * and forth between computers.
 * @author Matt Crow
 */
public class ServerMessage implements JsonSerialable{
    private InetAddress ipAddr;
    private AbstractUser fromUser;
    private final String body;
    private final ServerMessageType type;
    
    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param ipAddress the IP address this was sent from
     * @param bodyText the text of the message
     * @param messageType an enum value representing the type of message this is.
     * the receiving servers will react to this message based upon this type
     */
    public ServerMessage(InetAddress ipAddress, String bodyText, ServerMessageType messageType){
        ipAddr = ipAddress;
        body = bodyText;
        type = messageType;
    }
    
    public ServerMessage(String bodyText, ServerMessageType messageType) throws UnknownHostException{
        this(
            InetAddress.getLocalHost(),
            bodyText,
            messageType
        );
        setSender(LocalUser.getInstance());
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
    
    public final void setSendingIp(InetAddress ip){
        ipAddr = ip;
    }
    public final InetAddress getSendingIp(){
        return ipAddr;
    }
    
    public final String getBody(){
        return body;
    }
    
    public final ServerMessageType getType(){
        return type;
    }

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        ret.add("type", type.toString());
        ret.add("from", ipAddr.toString());
        ret.add("body", body);
        return ret.build();
    }
    
    /**
     * De-serializes an object, then converts it to a ServerMessage
     * @param obj the JsonObject to de-serialize
     * @return the ServerMessage encoded as obj
     * @throws JsonException if the JsonObject is not a ServerMessage
     */
    public static ServerMessage deserializeJson(JsonObject obj) throws JsonException{
        JsonUtil.verify(obj, "type");
        JsonUtil.verify(obj, "from");
        JsonUtil.verify(obj, "body");
        return new ServerMessage(
            (InetAddress)obj.getJsonObject("from"),
            obj.getString("body"),
            ServerMessageType.fromString(obj.getString("type"))
        );
    }
    
    /**
     * Attempts to convert a string to a JsonObject,
     * then attempts to convert that JsonObject to a ServerMessage
     * @param s
     * @return
     * @throws JsonException 
     */
    public static ServerMessage deserializeJson(String s) throws JsonException{
        return deserializeJson(Json.createReader(new StringReader(s)).readObject());
    }
    
    public String toJsonString(){
        return serializeJson().toString();
    }
    
    public void displayData(){
        System.out.println("From: " + ipAddr);
        if(fromUser != null){
            System.out.println(fromUser);
        }
        System.out.println("Type: " + type.toString());
        System.out.println("Body: " + body);
        System.out.println("END OF MESSAGE");
    }
}
