package windows.start;

import java.awt.GridLayout;
import javax.swing.JButton;
import windows.Page;
import windows.WorldSelect.WSMain;
import windows.customize.CustomizeMain;

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
