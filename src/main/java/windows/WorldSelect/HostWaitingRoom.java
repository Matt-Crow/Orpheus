package windows.WorldSelect;

import battle.Battle;
import java.io.IOException;
import net.OrpheusServer;
import net.protocols.WaitingRoomHostProtocol;

/**
 *
 * @author Matt
 */
public class HostWaitingRoom extends AbstractWaitingRoom{
    public HostWaitingRoom(Battle game) throws IOException{
        super();
        WaitingRoomHostProtocol protocol = new WaitingRoomHostProtocol(this, OrpheusServer.getInstance(), game);
        setBackEnd(protocol);
    }

    @Override
    public void startButton() {
        ((WaitingRoomHostProtocol)getBackEnd()).prepareToStart();
        getChat().log("The game will start in " + WaitingRoomHostProtocol.WAIT_TIME + " seconds. Please select your build and team.");
    }
}
