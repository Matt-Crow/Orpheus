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
        System.out.println("logged in");
        getHostingPage().switchToSubpage(WorldSelectCanvas.WAIT);
        System.out.println("switched page");
        Master.getUser().initPlayer();
        System.out.println("init player");
        Master.getUser().getPlayer().applyBuild(getSelectedBuild());
        System.out.println("applied build");
        SubPage sp = getHostingPage().getCurrentSubPage();
        if(sp instanceof WSWaitForPlayers){
            System.out.println("is wait");
            WSWaitForPlayers wait = (WSWaitForPlayers)sp;
            wait.startServer();
            System.out.println("server started");
            wait.setTeamSize(getTeamSize());
            System.out.println("team size set: " + getTeamSize());
            wait.joinTeam1(Master.getUser());
            System.out.println("join team 1");
        }
    }
}
