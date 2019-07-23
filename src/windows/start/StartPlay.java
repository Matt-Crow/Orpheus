package windows.start;

import controllers.MainWindow;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import windows.Page;
import windows.SubPage;
import windows.WorldSelect.WorldSelectPage;
import windows.customize.CustomizePage;

/**
 *
 * @author Matt
 */
public class StartPlay extends Page{
    public StartPlay(MainWindow host){
        super(host);
        JPanel content = getContent();
        content.setLayout(new GridLayout(1, 2));
        
        JButton back = new JButton("Main Menu");
        back.addActionListener((e)->{
            getHost().switchToPage(new StartPage(getHost()));
        });
        addMenuItem(back);
        
        JButton battle = new JButton("Battle");
        battle.addActionListener((e)->{
            getHost().switchToPage(new WorldSelectPage(getHost()));
        });
        content.add(battle);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            getHost().switchToPage(new CustomizePage(getHost()));
        });
        content.add(customize);
    }
}
