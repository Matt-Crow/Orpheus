package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;
import net.protocols.WaitingRoomHostProtocol;

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
        Master.loginWindow();
        try{
            Master.getUser().initPlayer().getPlayer().applyBuild(getSelectedBuild());
            HostWaitingRoom wait = new HostWaitingRoom(createBattle());
            wait.startProtocol();
            getHost().switchToPage(wait);
            ((WaitingRoomHostProtocol)wait.getBackEnd()).addUserToTeam(Master.getUser());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
