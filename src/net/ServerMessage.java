package net;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;

/**
 *
 * @author Matt
 */
public class ServerMessage implements JsonSerialable{
    
    // Enum constants: used to designate what type of message this is
    public static final int CHAT_MESSAGE = 0;
    public static final int WAITING_ROOM_UPDATE = 1;
    public static final int PLAYER_JOINED = 2;
    public static final int PLAYER_LEFT = 3;
    public static final int OTHER = -1;
    
    private final String fromIpAddr;
    private final String body;
    private final int type;
    
    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param ipAddr the IP address this is sent from
     * @param bodyText the text of the message
     * @param messageType an enum value representing the type of message this is:
     * <br>
     * <ul>
     * <li>ServerMessage.CHAT_MESSAGE</li>
     * <li>ServerMessage.WAITING_ROOM_UPDATE</li>
     * <li>ServerMessage.PLAYER_JOINED</li>
     * <li>ServerMessage.PLAYER_LEFT</li>
     * <li>ServerMessage.OTHER</li>
     * </ul>
     */
    public ServerMessage(String ipAddr, String bodyText, int messageType){
        fromIpAddr = ipAddr;
        body = bodyText;
        type = messageType;
    }
    
    public ServerMessage(String bodyText, int messageType) throws UnknownHostException{
        this(
            InetAddress.getLocalHost().getHostAddress(),
            bodyText,
            messageType
        );
    }
    
    public final String getSenderIpAddr(){
        return fromIpAddr;
    }
    
    public final String getBody(){
        return body;
    }
    
    public final int getType(){
        return type;
    }

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        ret.add("type", type);
        ret.add("from", fromIpAddr);
        ret.add("body", body);
        return ret.build();
    }
    
    public String toJsonString(){
        return serializeJson().toString();
    }
}
