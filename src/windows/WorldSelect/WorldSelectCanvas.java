package windows.WorldSelect;

import java.awt.CardLayout;
import javax.swing.*;
import windows.MainCanvas;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WorldSelectCanvas extends Page{
    public static final String MAIN = "MAIN";
    public static final String SOLO = "SOLO";
    public static final String NEW_MULTIPLAYER = "NEW MULTIPLAYER";
    public static final String JOIN_MULTIPLAYER = "JOIN MULTIPLAYER";
    public static final String WAIT = "WAIT";
    public WorldSelectCanvas(){
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
        addSubPage(JOIN_MULTIPLAYER, new SubPage(this));
        addSubPage(WAIT, new WSWaitForPlayers(this));
    }
}
