package orpheus.core.net.messages;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import orpheus.core.users.User;

public class MessageTester {
    
    @Test
    public void givenAMessage_afterSerializingWithSender_canDeserialize() {
        User aUser = new User("Foo");
        MessageType aType = MessageType.NEW_WAITING_ROOM;
        JsonObject aBody = Json.createObjectBuilder().build();
        Message aMessage = new Message(aUser, aType, aBody);

        JsonObject afterSerializing = aMessage.toJson();
        Message deserialized = Message.fromJson(afterSerializing);

        Assertions.assertEquals(aUser, deserialized.getSender().get());
        Assertions.assertEquals(aType, deserialized.getMessageType());
        Assertions.assertEquals(aBody, deserialized.getBody());
    }

    @Test
    public void givenAMessage_afterSerializingWithoutSender_canDeserialize() {
        MessageType aType = MessageType.NEW_WAITING_ROOM;
        JsonObject aBody = Json.createObjectBuilder().build();
        Message aMessage = new Message(aType, aBody);

        JsonObject afterSerializing = aMessage.toJson();
        Message deserialized = Message.fromJson(afterSerializing);

        Assertions.assertTrue(deserialized.getSender().isEmpty());
        Assertions.assertEquals(aType, deserialized.getMessageType());
        Assertions.assertEquals(aBody, deserialized.getBody());
    }
}
