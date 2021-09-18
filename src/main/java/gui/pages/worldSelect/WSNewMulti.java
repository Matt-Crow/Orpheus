package gui.pages.worldSelect;

import java.io.IOException;
import net.OrpheusServer;
import net.ServerProvider;
import net.protocols.WaitingRoomHostProtocol;
import users.LocalUser;

/**
 *
 * @author Matt Crow
 */
public class WSNewMulti extends AbstractWSNewWorld{
    public WSNewMulti(){
        super();
    }
    
    @Override
    public void start(){
        LocalUser.getInstance().loginWindow();
        try{
            OrpheusServer server = new ServerProvider().createHost();
            HostWaitingRoom wait = new HostWaitingRoom(server, createBattle());
            wait.startProtocol();
            getHost().switchToPage(wait);
            ((WaitingRoomHostProtocol)wait.getBackEnd()).addUserToTeam(LocalUser.getInstance());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
