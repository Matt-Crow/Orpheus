package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;

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
            HostWaitingRoom wait = new HostWaitingRoom(getNumWaves(), getMaxEnemyLevel());
            getHost().switchToPage(wait);
            Master.getUser().initPlayer();
            Master.getUser().getPlayer().applyBuild(getSelectedBuild());
            wait.joinPlayerTeam(Master.getUser());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
