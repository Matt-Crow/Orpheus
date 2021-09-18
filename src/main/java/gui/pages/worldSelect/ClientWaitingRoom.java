package gui.pages.worldSelect;

import net.protocols.WaitingRoomClientProtocol;

/**
 *
 * @author Matt
 */
public class ClientWaitingRoom extends AbstractWaitingRoom{
    public ClientWaitingRoom(String hostIp, int hostPort) {
        super();
        setBackEnd(new WaitingRoomClientProtocol(this, hostIp, hostPort));
    }
    
    @Override
    public void startButton() {
        getChat().logLocal("only the host can start the world. You'll have to wait for them.");
        getChat().log("Are we waiting on anyone?");
    }

}
