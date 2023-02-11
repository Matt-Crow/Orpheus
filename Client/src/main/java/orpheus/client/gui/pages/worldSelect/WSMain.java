package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.components.Style;
import java.awt.GridLayout;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.start.StartPlay;
import orpheus.client.gui.pages.PageController;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends Page{    
    public WSMain(ClientAppContext context, PageController host){
        super(context, host);
        var cf = context.getComponentFactory();
        addBackButton(()-> new StartPlay(context, host));
        setLayout(new GridLayout(1, 3));
        
        add(cf.makeButton("Play a game offline", ()->{
            getHost().switchToPage(new WSSolo(context, getHost()));
        }));
        
        add(cf.makeButton("Host a multiplayer game", ()->{
            getHost().switchToPage(new WSNewMulti(context, getHost()));
        }));
        add(cf.makeButton("Join a multiplayer game", ()->{
            getHost().switchToPage(new WSJoin(context, getHost()));
        }));
        Style.applyStyling(this);
    }
}
