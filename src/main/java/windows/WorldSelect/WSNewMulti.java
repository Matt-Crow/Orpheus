package windows.WorldSelect;

import controllers.User;
import java.io.IOException;
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
        User.loginWindow();
        try{
            User.getUser().initPlayer().getPlayer().applyBuild(getSelectedBuild());
            HostWaitingRoom wait = new HostWaitingRoom(createBattle());
            wait.startProtocol();
            getHost().switchToPage(wait);
            ((WaitingRoomHostProtocol)wait.getBackEnd()).addUserToTeam(LocalUser.getInstance());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
