package windows.start;

import java.awt.GridLayout;
import javax.swing.JButton;
import windows.Page;
import windows.SubPage;
import windows.WorldSelect.WorldSelectPage;
import windows.customize.CustomizePage;

/**
 *
 * @author Matt
 */
public class StartPlay extends SubPage{
    public StartPlay(Page p){
        super(p);
        setLayout(new GridLayout(1, 2));
        
        JButton battle = new JButton("Battle");
        battle.addActionListener((e)->{
            getHostingPage().getHost().switchToPage(new WorldSelectPage(p.getHost()));
        });
        add(battle);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            getHostingPage().getHost().switchToPage(new CustomizePage(p.getHost()));
        });
        add(customize);
    }
}
