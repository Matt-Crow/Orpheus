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
    private WaitingRoomHostProtocol protocol;
    
    public HostWaitingRoom(){
        protocol = null;
    }
    
    public void startServer(int waveCount, int enemyLv) throws IOException{
        OrpheusServer server = Master.SERVER;
        if(server.isStarted()){
            server.reset();
        } else {
            server.start();
        }
        protocol = new WaitingRoomHostProtocol(this, waveCount, enemyLv);
        server.setProtocol(protocol);
        
        getChat().openChatServer();
        getChat().logLocal("Server started on host address " + server.getIpAddr());
    }
    
    public void joinPlayerTeam(User u){
        protocol.addUserToTeam(u, Master.SERVER);
        getChat().logLocal(u.getName() + " has joined the team.");
        updateTeamDisplays();
    }
    
}
