package orpheus.core.net.protocols;

import java.io.IOException;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.AbstractNetworkClient;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.MessageHandler;
import orpheus.core.net.messages.Message;

public class MessageHandlerTester {
    
    @Test
    public void receive_givenNoConfiguration_returnsFalse() {
        var sut = new MessageHandler(new NetworkClientMock());
        var actual = sut.handleMessage(new ServerMessagePacket(null, new Message(ServerMessageType.CHAT)));
        Assertions.assertFalse(actual);
    }

    @Test
    public void receive_givenHandler_handlesMessagesOfThatType() {
        var sut = new MessageHandler(new NetworkClientMock());
        var callChecker = new CallChecker();
        sut.addHandler(ServerMessageType.CHAT, callChecker);

        var actual = sut.handleMessage(new ServerMessagePacket(null, new Message(ServerMessageType.CHAT)));
    
        Assertions.assertTrue(actual);
        Assertions.assertTrue(callChecker.wasCalled());
    }

    @Test
    public void receive_givenHandler_doesNotHandleMessagesOfOtherTypes() {
        var sut = new MessageHandler(new NetworkClientMock());
        var callChecker = new CallChecker();
        sut.addHandler(ServerMessageType.PLAYER_JOINED, callChecker);

        var actual = sut.handleMessage(new ServerMessagePacket(null, new Message(ServerMessageType.CHAT)));
    
        Assertions.assertFalse(actual);
        Assertions.assertFalse(callChecker.wasCalled());
    }
}

class NetworkClientMock extends AbstractNetworkClient {

    @Override
    protected void doStart() throws IOException {
    
    }

    @Override
    protected void doStop() throws IOException {
    
    }

    @Override
    protected void doReceiveMessage(ServerMessagePacket sm) {
    
    }

    @Override
    public void send(Message sm) {
    
    }
}

class CallChecker implements Consumer<ServerMessagePacket> {
    private int timesCalled = 0;

    public boolean wasCalled() {
        return timesCalled != 0;
    }

    @Override
    public void accept(ServerMessagePacket t) {
        timesCalled++;
    }
}