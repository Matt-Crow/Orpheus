package orpheus.client.gui.pages.worldSelect;

import orpheus.client.gui.components.Style;
import java.awt.GridLayout;
import javax.swing.JButton;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.menu.StartPlay;
import orpheus.client.gui.pages.PageController;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends Page{
    private final ComponentFactory components;
    
    public WSMain(PageController host, ComponentFactory cf){
        super(host, cf);
        addBackButton(new StartPlay(host, cf));
        setLayout(new GridLayout(1, 3));
        
        components = cf;
        
        add(soloButton());
        add(newMultiButton());
        add(joinMultiButton());
        Style.applyStyling(this);
    }
    
    private JButton soloButton(){
        JButton solo = new JButton("Play a game offline");
        Style.applyStyling(solo);
        solo.addActionListener((e)->{
            getHost().switchToPage(new WSSolo(getHost(), getComponentFactory()));
        });
        return solo;
    }
    
    private JButton newMultiButton(){
        JButton newMulti = new JButton("Host a multiplayer game");
        Style.applyStyling(newMulti);
        newMulti.addActionListener((e)->{
            getHost().switchToPage(new WSNewMulti(getHost(), getComponentFactory()));
        });
        return newMulti;
    }
    
    private JButton joinMultiButton(){
        JButton joinMulti = new JButton("Join a multiplayer game");
        Style.applyStyling(joinMulti);
        joinMulti.addActionListener((e)->{
            getHost().switchToPage(new WSJoin(getHost(), components));
        });
        return joinMulti;
    }
}
