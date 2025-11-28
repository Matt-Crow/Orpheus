package orpheus.core.net.protocols;

import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import orpheus.core.net.messages.*;

public class MessageHandlerTester {
    
    @Test
    public void receive_givenNoConfiguration_returnsFalse() {
        var sut = new MessageHandler();
        var actual = sut.handleMessage(new Message(ServerMessageType.CHAT));
        Assertions.assertFalse(actual);
    }

    @Test
    public void receive_givenHandler_handlesMessagesOfThatType() {
        var sut = new MessageHandler();
        var callChecker = new CallChecker();
        sut.addHandler(ServerMessageType.CHAT, callChecker);

        var actual = sut.handleMessage(new Message(ServerMessageType.CHAT));
    
        Assertions.assertTrue(actual);
        Assertions.assertTrue(callChecker.wasCalled());
    }

    @Test
    public void receive_givenHandler_doesNotHandleMessagesOfOtherTypes() {
        var sut = new MessageHandler();
        var callChecker = new CallChecker();
        sut.addHandler(ServerMessageType.PLAYER_JOINED, callChecker);

        var actual = sut.handleMessage(new Message(ServerMessageType.CHAT));
    
        Assertions.assertFalse(actual);
        Assertions.assertFalse(callChecker.wasCalled());
    }
}

class CallChecker implements Consumer<Message> {
    private int timesCalled = 0;

    public boolean wasCalled() {
        return timesCalled != 0;
    }

    @Override
    public void accept(Message t) {
        timesCalled++;
    }
}