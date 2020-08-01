package net.messages;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 * 
 * @author Matt
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
        ret.add("body", body);
        return ret.build();
    }
    
    public String toJsonString(){
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
}
