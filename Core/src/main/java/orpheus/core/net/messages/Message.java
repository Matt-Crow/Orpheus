package orpheus.core.net.messages;

import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import orpheus.core.users.User;

/**
 * a message sent to or from a server
 */
public class Message {
    
    /**
     * the user who sent the message
     */
    private final Optional<User> sender;

    /**
     * the type of message this is - used to decide how to react to it
     */
    private final MessageType type;

    /**
     * the body of the message - formatted based upon the type
     */
    private final JsonObject body;

    /**
     * @param sender the user who sent this message
     * @param type determines how to process this message
     * @param body the content of the message
     */
    public Message(User sender, MessageType type, JsonObject body) {
        this.sender = Optional.of(sender);
        this.type = type;
        this.body = body;
    }

    /**
     * creates a message with an unknown sender
     * @param type determines how to process this message
     * @param body the content of the message
     */
    public Message(MessageType type, JsonObject body) {
        this.sender = Optional.empty();
        this.type = type;
        this.body = body;
    }

    /**
     * creates a message with no body
     * @param sender the user who sent this message
     * @param type determines how to process this message
     */
    public Message(User sender, MessageType type) {
        this(sender, type, JsonObject.EMPTY_JSON_OBJECT);
    }

    /**
     * creates a message with an unknown sender and an empty body
     * @param type determines how to process this message
     */
    public Message(MessageType type) {
        this(type, JsonObject.EMPTY_JSON_OBJECT);
    }

    /**
     * @return the user who sent this message
     */
    public Optional<User> getSender() {
        return sender;
    }

    /**
     * @return the type of message this is - used to decide how to react to it
     */
    public MessageType getMessageType() {
        return type;
    }

    /**
     * @return the body of the message - formatted based upon the type
     */
    public JsonObject getBody() {
        return body;
    }

    /**
     * converts this message to JSON
     * @return the JSON representation of this object
     */
    public JsonValue toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (sender.isPresent()) {
            builder.add("sender", sender.get().serializeJson());
        }
        builder.add("type", type.toString());
        builder.add("body", body);
        return builder.build();
    }

    public static Message fromJson(JsonValue json) {
        if (json.getValueType() != ValueType.OBJECT) {
            throw new IllegalArgumentException("can only be decoded from JSON object");
        }

        // todo convert to object, decode
        throw new RuntimeException("todo");
    }
}
