package orpheus.core.net;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.users.User;
import serialization.JsonSerialable;

public class ChatMessage implements JsonSerialable {

    /**
     * the user who sent this chat message
     */
    private final User sender;

    /**
     * the text content of the message
     */
    private final String message;

    /**
     * @param sender the user who sent this chat message
     * @param message the text content of the message
     */
    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    /**
     * @return the user who sent this chat message
     */
    public User getSender() {
        return sender;
    }

    /**
     * @return the text content of the message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("sender", sender.toJson())
            .add("message", message)
            .build();
    }
    
    public static ChatMessage fromJson(JsonObject json) {
        return new ChatMessage(
            User.fromJson(json.getJsonObject("sender")), 
            json.getString("message")
        );
    }
}
