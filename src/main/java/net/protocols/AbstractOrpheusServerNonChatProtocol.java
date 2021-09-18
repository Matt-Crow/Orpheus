package net.protocols;

import java.io.IOException;
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
    
    /**
     * Restarts the server, and applies this as its protocol
     * @throws IOException 
     */
    @Override
    public final void applyProtocol() throws IOException{
        getServer().restart();
        getServer().setProtocol(this);
        doApplyProtocol();
    }
}
