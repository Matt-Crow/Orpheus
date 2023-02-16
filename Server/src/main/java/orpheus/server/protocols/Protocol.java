package orpheus.server.protocols;

import orpheus.core.net.messages.Message;
import orpheus.server.OrpheusServer;
import orpheus.server.connections.Connection;

/**
 * a protocol running on a server, which decides how to handle messages
 */
public interface Protocol {
    
    /**
     * handles the given message
     * @param server the server the message was received on
     * @param from the connection this message was received from
     * @param message the message to react to
     */
    public void receive(OrpheusServer server, Connection from, Message message);
}
