package net.protocols;

import net.OrpheusServer;

/**
 * This class should be used to clarify exactly
 * what the OrpheusServer should do with messages
 * at any given time.
 * 
 * @author Matt Crow
 */
public abstract class AbstractOrpheusServerNonChatProtocol extends AbstractOrpheusServerProtocol{

    public AbstractOrpheusServerNonChatProtocol(OrpheusServer runningServer) {
        super(runningServer);
    }
}
