package net.messages;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 * The ServerMessage class is used to store data sent
 * between computers. Servers send these to their clients
 * as JSON, which gets deserialized as a ServerMessagePacket
 * when received by a Connection.
 * 
 * Basically, send as a ServerMessage, receive as a ServerMessagePacket
 * 
 * @author Matt Crow
 */
public class ServerMessage implements JsonSerialable {
    private final String body;
    private final ServerMessageType type;
    
    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param bodyText the text of the message
     * @param messageType an enum value representing the type of message this is.
     * the receiving servers will react to this message based upon this type
     */
    public ServerMessage(String bodyText, ServerMessageType messageType){
        body = bodyText;
        type = messageType;
    }
    
    /**
     * 
     * @return the body text of
     * this message.
     */
    public final String getBody(){
        return body;
    }
    
    /**
     * 
     * @return the type of message this is.
     * Used to control how the current protocol
     * handles this message.
     */
    public final ServerMessageType getType(){
        return type;
    }
    
    /**
     * Serializes this to a JsonObject so
     * it can be turned into a JSON string,
     * then sent to a client.
     * 
     * @return a JSON representation of this. 
     */
    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        ret.add("type", type.toString());
        ret.add("body", body);
        return ret.build();
    }
    
    
    public final String toJsonString(){
        return serializeJson().toString();
    }
    
    @Override
    public String toString(){
        return String.format("ServerMessage of type %s: %s", type.toString(), body);
    }
    
    /**
     * De-serializes an object, then converts it to a ServerMessage
     * @param obj the JsonObject to de-serialize
     * @return the ServerMessage encoded a json object
     * @throws JsonException if the JsonObject is not a ServerMessage
     */
    public static ServerMessage deserializeJson(JsonObject obj){
        JsonUtil.verify(obj, "type");
        JsonUtil.verify(obj, "body");
        return new ServerMessage(
            obj.getString("body"),
            ServerMessageType.fromString(obj.getString("type"))
        );
    }
    
    public static ServerMessage deserializeJson(String s){
        JsonObject obj = Json.createReader(new StringReader(s)).readObject();
        return deserializeJson(obj);
    }
}
