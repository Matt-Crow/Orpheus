package orpheus.server.connections;

import java.io.IOException;
import java.util.function.Consumer;

import orpheus.core.net.messages.Message;

/**
 * listens for messages from a connection
 */
public class MessageListenerThread {
    
    /**
     * the connection this is listening to
     */
    private final Connection listeningTo;

    /**
     * handles messages received from the connection
     */
    private final Consumer<Message> handleMessage;

    /**
     * the thread this is listening on
     */
    private final Thread thread;

    /**
     * whether this should continue listening for messages
     */
    private boolean listening;

    public MessageListenerThread(Connection listeningTo, Consumer<Message> handleMessage) {
        this.listeningTo = listeningTo;
        this.handleMessage = handleMessage;
        listening = true;
        thread = new Thread(this::listen);
        thread.start();
    }

    private void listen() {
        try {
            tryListen();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void tryListen() throws IOException {
        while (listening) {
            var message = listeningTo.receive();
            handleMessage.accept(message);
        }
    }

    public void stop() {
        listening = false;
    }
}
