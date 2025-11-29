package orpheus.core.net;

import orpheus.core.users.User;
import orpheus.core.utils.EventEmitter;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Represents a connection to either a client or a server running Orpheus
 * @author Matt Crow
 */
// TODO add local connection subclass
public abstract class Connection {
    private User remoteUser;
    private final EventEmitter<MessageReceivedEvent> eventMessageReceived = new EventEmitter<>();

    /**
     * @param u the user playing Orpheus on the machine this connects to
     */
    public void setRemoteUser(User u){
        remoteUser = u;
    }

    /**
     * @return the user playing Orpheus on the machine this connects to
     */
    public User getRemoteUser(){
        return remoteUser;
    }

    /**
     * Adds a listener this will notify when it receives a message
     * @param listener the listener to add
     */
    public void addMessageReceivedListener(Consumer<MessageReceivedEvent> listener) {
        eventMessageReceived.addEventListener(listener);
    }

    /**
     * Subclasses should call this method whenever they receive a message from someone else
     * @param message the message received
     */
    protected void receiveMessage(Message message) {
        eventMessageReceived.emitEvent(new MessageReceivedEvent(this, message));
    }
    
    // TODO don't throw IOException
    public abstract void writeServerMessage(Message sm) throws IOException;
    
    // TODO not needed for local connection?
    public abstract void close();
}
