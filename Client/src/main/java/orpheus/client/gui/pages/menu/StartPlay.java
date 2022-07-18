package orpheus.client.gui.pages.menu;

import java.awt.GridLayout;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.worldSelect.WSMain;
import orpheus.client.gui.pages.customize.CustomizeMain;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class StartPlay extends Page{
    public StartPlay(PageController host, ComponentFactory cf){
        super(host, cf);
        setLayout(new GridLayout(1, 2));
        
        addBackButton("Main Menu", new Index(host, cf));
        
        add(cf.makeButton("Play a game", ()->{
            getHost().switchToPage(new WSMain(host, cf));
        }));
        
        add(cf.makeButton("Customize builds", ()->{
            getHost().switchToPage(new CustomizeMain(host, cf));
        }));
    }
}
