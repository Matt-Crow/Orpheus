package net;

import controllers.Master;
import controllers.User;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 * The ServerMessage class is used to send information back
 * and forth between computers.
 * <h2>Possible types</h2>
 * <ul>
 * <li>ServerMessage.CHAT_MESSAGE</li>
 * <li>ServerMessage.WAITING_ROOM_UPDATE</li>
 * <li>ServerMessage.PLAYER_JOINED</li>
 * <li>ServerMessage.PLAYER_LEFT</li>
 * <li>ServerMessage.OTHER</li>
 * </ul>
 * 
 * @author Matt Crow
 */
public class ServerMessage implements JsonSerialable{
    
    // Enum constants: used to designate what type of message this is
    public static final int CHAT_MESSAGE = 0;
    public static final int WAITING_ROOM_UPDATE = 1;
    public static final int PLAYER_JOINED = 2;
    public static final int PLAYER_LEFT = 3;
    public static final int OTHER = -1;
    
    private final User fromUser;
    private final String body;
    private final int type;
    
    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param sender the User this is sent from
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
    public ServerMessage(User sender, String bodyText, int messageType){
        fromUser = sender;
        body = bodyText;
        type = messageType;
    }
    
    public ServerMessage(String bodyText, int messageType){
        this(
            Master.getUser(),
            bodyText,
            messageType
        );
    }
    
    public final User getSender(){
        return fromUser;
    }
    
    /*
    public final String getSenderIpAddr(){
        return fromIpAddr;
    }*/
    
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
        ret.add("from", fromUser.serializeJson());
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
            User.deserializeJson(obj.getJsonObject("from")),
            obj.getString("body"),
            obj.getInt("type")
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
        System.out.println("From: " + fromUser.getName());
        System.out.println("Type: " + type);
        System.out.println("Body: " + body);
        System.out.println("END OF MESSAGE");
    }
}
