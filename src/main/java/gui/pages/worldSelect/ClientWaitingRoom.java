package gui.pages.worldSelect;

import net.OrpheusServer;
import net.protocols.WaitingRoomClientProtocol;

/**
 *
 * @author Matt
 */
public class ClientWaitingRoom extends AbstractWaitingRoom{
    public ClientWaitingRoom(OrpheusServer runningServer) {
        super();
        setBackEnd(new WaitingRoomClientProtocol(runningServer, this));
    }
    
    @Override
    public void startButton() {
        getChat().logLocal("only the host can start the world. You'll have to wait for them.");
        getChat().log("Are we waiting on anyone?");
    }

}
