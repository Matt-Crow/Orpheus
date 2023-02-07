package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.components.Style;
import java.awt.GridLayout;

import orpheus.client.AppContext;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.start.StartPlay;
import orpheus.client.gui.pages.PageController;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends Page{    
    public WSMain(AppContext context, PageController host, ComponentFactory cf){
        super(context, host, cf);
        addBackButton(()-> new StartPlay(context, host, cf));
        setLayout(new GridLayout(1, 3));
        
        add(cf.makeButton("Play a game offline", ()->{
            getHost().switchToPage(new WSSolo(context, getHost(), cf));
        }));
        
        add(cf.makeButton("Host a multiplayer game", ()->{
            getHost().switchToPage(new WSNewMulti(context, getHost(), cf));
        }));
        add(cf.makeButton("Join a multiplayer game", ()->{
            getHost().switchToPage(new WSJoin(context, getHost(), cf));
        }));
        Style.applyStyling(this);
    }
}
