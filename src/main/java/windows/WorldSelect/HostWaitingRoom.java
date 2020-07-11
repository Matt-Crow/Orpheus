package windows.WorldSelect;

import controllers.Master;
import controllers.User;
import java.io.IOException;
import net.OrpheusServer;
import net.protocols.WaitingRoomHostProtocol;

/**
 *
 * @author Matt
 */
public class HostWaitingRoom extends AbstractWaitingRoom{
    public HostWaitingRoom(int waveCount, int enemyLv) throws IOException{
        super();
        OrpheusServer server = Master.SERVER;
        if(server.isStarted()){
            server.reset();
        } else {
            server.start();
        }
        WaitingRoomHostProtocol protocol = new WaitingRoomHostProtocol(this, server, waveCount, enemyLv);
        getChat().openChatServer();
        getChat().logLocal("Server started on host address " + server.getIpAddr());
        setBackEnd(protocol);
    }
    
    public void joinPlayerTeam(User u){
        ((WaitingRoomHostProtocol)getBackEnd()).addUserToTeam(u);
        getChat().logLocal(u.getName() + " has joined the team.");
        updateTeamDisplays();
    }

    @Override
    public void startButton() {
        ((WaitingRoomHostProtocol)getBackEnd()).prepareToStart();
        getChat().log("The game will start in " + WaitingRoomBackend.WAIT_TIME + " seconds. Please select your build and team.");
    }
}
