package orpheus.client.gui.pages.mainMenu;

import java.awt.GridLayout;
import javax.swing.JButton;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.worldSelect.WSMain;
import orpheus.client.gui.pages.customize.CustomizeMain;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class StartPlay extends Page{
    public StartPlay(PageController host){
        super(host);
        setLayout(new GridLayout(1, 2));
        
        addBackButton("Main Menu", new StartMainMenu(host));
        
        JButton battle = new JButton("Battle");
        battle.addActionListener((e)->{
            getHost().switchToPage(new WSMain(host));
        });
        add(battle);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            getHost().switchToPage(new CustomizeMain(host));
        });
        add(customize);
    }
}
