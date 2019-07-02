package windows.WorldSelect;

import javax.swing.*;
import windows.Page;

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
    public WorldSelectPage(){
        super();
        //addBackButton(new MainCanvas());
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
