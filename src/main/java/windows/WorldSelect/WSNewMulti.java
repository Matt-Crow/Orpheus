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
        WSWaitForPlayers wait = new WSWaitForPlayers();
        getHost().switchToPage(wait);
        Master.getUser().initPlayer();
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        try {
            wait.startServerAsHost(getNumWaves(), getMaxEnemyLevel());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wait.joinPlayerTeam(Master.getUser());
    }
}
