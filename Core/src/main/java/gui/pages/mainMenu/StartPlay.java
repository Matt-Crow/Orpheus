package gui.pages.mainMenu;

import java.awt.GridLayout;
import javax.swing.JButton;
import gui.pages.Page;
import gui.pages.worldSelect.WSMain;
import gui.pages.customize.CustomizeMain;

/**
 *
 * @author Matt
 */
public class StartPlay extends Page{
    public StartPlay(){
        super();
        setLayout(new GridLayout(1, 2));
        
        addBackButton("Main Menu", new StartMainMenu());
        
        JButton battle = new JButton("Battle");
        battle.addActionListener((e)->{
            getHost().switchToPage(new WSMain());
        });
        add(battle);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            getHost().switchToPage(new CustomizeMain());
        });
        add(customize);
    }
}
