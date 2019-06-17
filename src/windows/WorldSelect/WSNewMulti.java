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
        getHostingPage().switchToSubpage(WorldSelectCanvas.WAIT);
        Master.TRUEPLAYER.applyBuild(getSelectedBuild());
        SubPage sp = getHostingPage().getCurrentSubPage();
        if(sp instanceof WSWaitForPlayers){
            ((WSWaitForPlayers)sp).setTeamSize(getTeamSize()).joinTeam1(Master.TRUEPLAYER);
        }
    }
}
