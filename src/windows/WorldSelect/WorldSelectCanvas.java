package windows.WorldSelect;

import java.awt.CardLayout;
import javax.swing.*;
import windows.MainCanvas;
import windows.Page;

/**
 *
 * @author Matt
 */
public class WorldSelectCanvas extends Page{
    private final String MAIN = "MAIN";
    private final String SOLO = "SOLO";
    private final String NEW_MULTIPLAYER = "NEW MULTIPLAYER";
    private final String JOIN_MULTIPLAYR = "JOIN MULTIPLAYER";
    public WorldSelectCanvas(){
        super();
        //addBackButton(new MainCanvas());
        
        addSubPage(MAIN, new WSMain(this));
    }
}
