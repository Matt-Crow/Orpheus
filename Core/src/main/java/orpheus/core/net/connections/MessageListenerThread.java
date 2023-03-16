package orpheus.core.net.connections;

import java.io.IOException;
import java.util.function.Consumer;

import orpheus.core.net.messages.Message;

/**
 * Listens for messages from a connection.
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
        } catch(IOException ex) {
            // sadly, it doesn't look like there's a better way of handling this
            if (ex.getMessage().equals("Stream closed")) {
                //System.out.println("stream closed: stopping");
                stop();
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    private void tryListen() throws IOException {
        while (listening) {
            var message = listeningTo.receive();
            handleMessage.accept(message);
        }
    }

    /**
     * Notifies the thread to terminate after its next listening attempt 
     * succeeds or fails - does not immediately stop the thread. It is not an
     * error to call this method multiple times.
     */
    public void stop() {
        listening = false;
    }
}
