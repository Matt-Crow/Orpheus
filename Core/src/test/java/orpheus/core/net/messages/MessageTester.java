package orpheus.core.net.messages;

import javax.json.Json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.messages.ServerMessageType;
import orpheus.core.users.User;

public class MessageTester {
    
    @Test
    public void givenAMessage_afterSerializingWithSender_canDeserialize() {
        var aUser = new User("Foo");
        var aType = ServerMessageType.NEW_WAITING_ROOM;
        var aBody = Json.createObjectBuilder().build();
        var aMessage = new Message(aType, aBody, aUser);

        var afterSerializing = aMessage.toJson();
        var deserialized = Message.deserializeJson(afterSerializing);

        Assertions.assertEquals(aUser, deserialized.getSender().get());
        Assertions.assertEquals(aType, deserialized.getType());
        Assertions.assertEquals(aBody, deserialized.getBody());
    }

    @Test
    public void givenAMessage_afterSerializingWithoutSender_canDeserialize() {
        var aType = ServerMessageType.NEW_WAITING_ROOM;
        var aBody = Json.createObjectBuilder().build();
        var aMessage = new Message(aType, aBody);

        var afterSerializing = aMessage.toJson();
        var deserialized = Message.deserializeJson(afterSerializing);

        Assertions.assertTrue(deserialized.getSender().isEmpty());
        Assertions.assertEquals(aType, deserialized.getType());
        Assertions.assertEquals(aBody, deserialized.getBody());
    }
}
