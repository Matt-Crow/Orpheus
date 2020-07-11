package windows.WorldSelect;

import battle.Battle;
import controllers.Master;
import controllers.User;
import java.io.IOException;
import net.OrpheusServer;
import net.protocols.WaitingRoomHostBuildProtocol;
import net.protocols.WaitingRoomHostProtocol;

/**
 *
 * @author Matt
 */
public class HostWaitingRoom extends AbstractWaitingRoom{
    public HostWaitingRoom(Battle game) throws IOException{
        super();
        OrpheusServer server = Master.SERVER;
        if(server.isStarted()){
            server.reset();
        } else {
            server.start();
        }
        WaitingRoomHostProtocol protocol = new WaitingRoomHostProtocol(this, server, game);
        getChat().openChatServer();
        getChat().logLocal("Server started on host address " + server.getIpAddr());
        setBackEnd(protocol);
    }

    @Override
    public void startButton() {
        ((WaitingRoomHostProtocol)getBackEnd()).prepareToStart();
        getChat().log("The game will start in " + WaitingRoomHostBuildProtocol.WAIT_TIME + " seconds. Please select your build and team.");
    }
}
