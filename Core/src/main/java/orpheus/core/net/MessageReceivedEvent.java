package orpheus.core.net;

/**
 * Emitted by a Connection whenever it receives a message
 */
public class MessageReceivedEvent {
    private final Connection fromConnection;
    private final Message message;

    /**
     * @param fromConnection the Connection which emitted this event
     * @param message the Message the Connection received
     */
    public MessageReceivedEvent(Connection fromConnection, Message message) {
        this.fromConnection = fromConnection;
        this.message = message;
    }

    /**
     * @return the Connection which emitted this event
     */
    public Connection getConnection() {
        return fromConnection;
    }

    /**
     * @return the Message received
     */
    public Message getMessage() {
        return message;
    }
}
