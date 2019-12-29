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
            wait.startServerAsHost();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        wait.setNumWaves(getNumWaves());
        wait.setMaxEnemyLevel(getMaxEnemyLevel());
        wait.joinPlayerTeam(Master.getUser());
    }
}
