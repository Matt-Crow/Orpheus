package net.protocols;

import net.AbstractNetworkClient;

/**
 * This class should be used to clarify exactly
 * what the OrpheusServer should do with messages
 * at any given time.
 * 
 * @author Matt Crow
 * @param <T> the type of client that will run this
 */
public abstract class AbstractOrpheusServerNonChatProtocol extends AbstractProtocol {

    public AbstractOrpheusServerNonChatProtocol(AbstractNetworkClient runningServer) {
        super(runningServer);
    }
}
