package windows.WorldSelect;

import controllers.Master;
import net.OrpheusServer;
import net.protocols.WaitingRoomClientProtocol;

/**
 *
 * @author Matt
 */
public class ClientWaitingRoom extends AbstractWaitingRoom{
    public ClientWaitingRoom(String hostIp) {
        super();
        setBackEnd(new WaitingRoomClientProtocol(this, OrpheusServer.getInstance(), hostIp));
    }
    
    @Override
    public void startButton() {
        getChat().logLocal("only the host can start the world. You'll have to wait for them.");
        getChat().log("Are we waiting on anyone?");
    }

}
