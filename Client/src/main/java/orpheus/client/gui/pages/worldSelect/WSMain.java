package orpheus.client.gui.pages.worldSelect;

import orpheus.client.gui.components.Style;
import java.awt.GridLayout;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.start.StartPlay;
import orpheus.client.gui.pages.PageController;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends Page{    
    public WSMain(PageController host, ComponentFactory cf){
        super(host, cf);
        addBackButton(new StartPlay(host, cf));
        setLayout(new GridLayout(1, 3));
        
        add(cf.makeButton("Play a game offline", ()->{
            getHost().switchToPage(new WSSolo(getHost(), cf));
        }));
        
        add(cf.makeButton("Host a multiplayer game", ()->{
            getHost().switchToPage(new WSNewMulti(getHost(), cf));
        }));
        add(cf.makeButton("Join a multiplayer game", ()->{
            getHost().switchToPage(new WSJoin(getHost(), cf));
        }));
        Style.applyStyling(this);
    }
}
