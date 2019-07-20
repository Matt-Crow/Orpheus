package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt Crow
 */
public class WSNewMulti extends AbstractWSNewWorld{
    public WSNewMulti(Page p){
        super(p);
    }
    
    @Override
    public void start(){
        Master.loginWindow();
        getHostingPage().switchToSubpage(WorldSelectPage.WAIT);
        Master.getUser().initPlayer();
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        SubPage sp = getHostingPage().getCurrentSubPage();
        if(sp instanceof WSWaitForPlayers){
            WSWaitForPlayers wait = (WSWaitForPlayers)sp;
            try {
                wait.startServerAsHost();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            wait.setTeamSize(getTeamSize());
            wait.joinTeam1(Master.getUser());
        }
    }
}
