package orpheus.core.net;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;

import orpheus.core.users.User;
import serialization.JsonSerialable;

/**
 * The ServerMessage class is used to store data sent
 * between computers.
 * 
 * @author Matt Crow
 */
public class Message implements JsonSerialable {

    /**
     * the user who sent the message
     */
    private final Optional<User> sender;

    /**
     * the type of message this is - used to describe how the receiver should
     * handle it
     */
    private final ServerMessageType type;

    /**
     * the contents of the message - formatted based upon type
     */
    private final JsonObject body;
    
    /**
     * @param type the type of message this is
     * @param body the JSON content of the message body
     * @param sender the user who sent this message
     */
    public Message(ServerMessageType type, JsonObject body, User sender) {
        this.sender = Optional.of(sender);
        this.body = body;
        this.type = type;
    }

    /**
     * Creates a ServerMessage with an anonymous sender.
     * @param type the type of this message
     * @param body the JSON contents of the message body
     */
    public Message(ServerMessageType type, JsonObject body) {
        this.sender = Optional.empty();
        this.type = type;
        this.body = body;
    }

    /**
     * Creates a message with no body and an anonymous sender
     * @param type tye type of message this is
     */
    public Message(ServerMessageType type) {
        this(type, JsonObject.EMPTY_JSON_OBJECT);
    }

    /**
     * Creates a message, which will be serialized before being sent to another
     * server.
     * 
     * @param bodyText the text of the message
     * @param messageType an enum value representing the type of message this is.
     * the receiving servers will react to this message based upon this type
     */
    public Message(String bodyText, ServerMessageType messageType){
        sender = Optional.empty();
        body = Json.createObjectBuilder()
            .add("text", bodyText)
            .build();
        type = messageType;
    }

    /**
     * Creates a copy of this message with the given user as the sender
     * @param me the new sender
     * @return a copy of this message, but with the new sender
     */
    public Message withSentBy(User me) {
        return new Message(type, body, me);
    }
    
    /**
     * @return the user who sent this message
     */
    public Optional<User> getSender() {
        return sender;
    }

    /**
     * 
     * @return the body text of this message.
     */
    public final String getBodyText(){
        return body.getString("text");
    }

    /**
     * @return the JSON object contained in the body of this message
     */
    public JsonObject getBody() {
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
    public JsonObject toJson() {
        var builder = Json.createObjectBuilder();
        if (sender.isPresent()) {
            builder.add("sender", sender.get().toJson());
        }
        builder.add("type", type.toString());
        builder.add("body", body);
        return builder.build();
    }
    
    
    public final String toJsonString(){
        return toJson().toString();
    }
    
    /**
     * De-serializes a JSON string, then converts it to a ServerMessage
     * @param obj the JsonObject to de-serialize
     * @return the ServerMessage encoded a json object
     * @throws JsonException if the JsonObject is not a ServerMessage
     */
    public static Message deserializeJson(String s){
        JsonObject obj = Json.createReader(new StringReader(s)).readObject();
        var type = ServerMessageType.fromString(obj.getString("type"));
        var body = obj.getJsonObject("body");
        return (obj.containsKey("sender"))
            ? new Message(type, body, User.fromJson(obj.getJsonObject("sender")))
            : new Message(type, body);
    }

    @Override
    public String toString(){
        var w = new StringWriter();
        var config = new HashMap<String, String>();
        config.put(JsonGenerator.PRETTY_PRINTING, "");
        Json.createWriterFactory(config)
            .createWriter(w)
            .writeObject(toJson());
        return w.toString();
    }
}
