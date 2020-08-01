package net.messages;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;

/**
 * The AbstractServerMessage class is used to replace the old ServerMessage
 * class. Orpheus uses this class to send messages between computers using
 * connections.
 * 
 * @author Matt Crow
 */
public abstract class AbstractServerMessage implements JsonSerialable {
    private final ServerMessageType type;
    private final String body;
    
    /**
     * Creates a message, which will be serialized to JSON before being sent to 
     * another server.
     * 
     * @param msgType an enum value representing the type of message this is.
     * the receiving servers will react to this message based upon this type
     * @param contents the text of the message
     */
    public AbstractServerMessage(ServerMessageType msgType, String contents){
        type = msgType;
        body = contents;
    }
    
    public final ServerMessageType getType(){
        return type;
    }
    
    public final String getBody(){
        return body;
    }
    
    /**
     * Converts this message into a JSON object for sending to
     * another server. Note that both Sent- and Received- messages
     * are serialized the same way, but will be deserialized
     * based on whether or not they were received through a
     * Connection.
     * 
     * @return a JSON representation of this server message
     */
    @Override
    public final JsonObject serializeJson() {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        ret.add("type", type.toString());
        ret.add("body", body);
        return ret.build();
    }
    
    public final String toJsonString(){
        return serializeJson().toString();
    }
}
