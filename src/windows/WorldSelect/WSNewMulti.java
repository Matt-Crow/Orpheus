package windows.WorldSelect;

import controllers.Master;
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
        getHostingPage().switchToSubpage(WorldSelectCanvas.WAIT);
        Master.getUser().initPlayer();
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        SubPage sp = getHostingPage().getCurrentSubPage();
        if(sp instanceof WSWaitForPlayers){
            ((WSWaitForPlayers)sp).startServer().setTeamSize(getTeamSize()).joinTeam1(Master.getUser());
        }
    }
}
