package windows.WorldSelect;

import net.protocols.AbstractWaitingRoomProtocol;

/**
 *
 * @author Matt
 */
public class ClientWaitingRoom extends AbstractWaitingRoom{

    public ClientWaitingRoom(String ipAddress) {
        super(null); // need protocol here
        // init client server
        // connect
        getChat().joinChat(ipAddress);
        getChat().logLocal("Connected to host " + ipAddress);
    }

    @Override
    public void startButton() {
        getChat().logLocal("only the host can start the world. You'll have to wait for them.");
        getChat().log("Are we waiting on anyone?");
    }

}
