package gui.pages.worldSelect;

import gui.components.Style;
import java.awt.GridLayout;
import javax.swing.JButton;
import gui.pages.Page;
import gui.pages.mainMenu.StartPlay;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends Page{
    public WSMain(){
        super();
        addBackButton(new StartPlay());
        setLayout(new GridLayout(1, 3));
        
        add(soloButton());
        add(newMultiButton());
        add(joinMultiButton());
        Style.applyStyling(this);
    }
    
    private JButton soloButton(){
        JButton solo = new JButton("Play a game offline");
        Style.applyStyling(solo);
        solo.addActionListener((e)->{
            getHost().switchToPage(new WSSolo());
        });
        return solo;
    }
    
    private JButton newMultiButton(){
        JButton newMulti = new JButton("Host a multiplayer game");
        Style.applyStyling(newMulti);
        newMulti.addActionListener((e)->{
            getHost().switchToPage(new WSNewMulti());
        });
        return newMulti;
    }
    
    private JButton joinMultiButton(){
        JButton joinMulti = new JButton("Join a multiplayer game");
        Style.applyStyling(joinMulti);
        joinMulti.addActionListener((e)->{
            getHost().switchToPage(new WSJoin());
        });
        return joinMulti;
    }
}
