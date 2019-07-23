package windows.start;

import java.awt.GridLayout;
import javax.swing.JButton;
import windows.NewPage;
import windows.WorldSelect.WorldSelectPage;
import windows.customize.CustomizePage;

/**
 *
 * @author Matt
 */
public class StartPlay extends NewPage{
    public StartPlay(){
        super();
        setLayout(new GridLayout(1, 2));
        
        addBackButton("Main Menu", new StartMainMenu());
        
        JButton battle = new JButton("Battle");
        battle.addActionListener((e)->{
            getHost().switchToPage(new WorldSelectPage(getHost()));
        });
        add(battle);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            getHost().switchToPage(new CustomizePage(getHost()));
        });
        add(customize);
    }
}
