package orpheus.core.net;

import orpheus.core.net.connections.Connection;
import orpheus.core.net.messages.Message;

/**
 * handles messages received by an OrpheusClient
 */
public interface ClientProtocol {
    
    /**
     * called whenever the client receives a message
     * @param client the client which received the message
     * @param from the connection the message was received from
     * @param message the message received
     */
    public void receive(OrpheusClient client, Connection from, Message message);
}
