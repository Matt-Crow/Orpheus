package windows.WorldSelect;

import controllers.MainWindow;
import javax.swing.*;
import windows.Page;
import windows.start.StartPage;

/**
 *
 * @author Matt
 */
public class WorldSelectPage extends Page{
    public static final String MAIN = "MAIN";
    public static final String SOLO = "SOLO";
    public static final String NEW_MULTIPLAYER = "NEW MULTIPLAYER";
    public static final String JOIN_MULTIPLAYER = "JOIN MULTIPLAYER";
    public static final String WAIT = "WAIT";
    public WorldSelectPage(MainWindow host){
        super(host);
        JButton back = new JButton("Go back");
        back.addActionListener((e)->{
            StartPage p = new StartPage(host);
            getHost().switchToPage(p);
            p.switchToSubpage(StartPage.PLAY);
        });
        addMenuItem(back);
        
        JButton title = new JButton("Select World Type");
        title.addActionListener((e)->{
            switchToSubpage(MAIN);
        });
        addMenuItem(title);
        
        addSubPage(MAIN, new WSMain(this));
        addSubPage(SOLO, new WSSolo(this));
        addSubPage(NEW_MULTIPLAYER, new WSNewMulti(this));
        addSubPage(JOIN_MULTIPLAYER, new WSJoin(this));
        addSubPage(WAIT, new WSWaitForPlayers(this));
    }
}
