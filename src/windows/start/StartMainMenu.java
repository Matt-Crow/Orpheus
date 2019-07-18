package windows.start;

import java.awt.GridLayout;
import javax.swing.JButton;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class StartMainMenu extends SubPage{
    public StartMainMenu(Page p){
        super(p);
        setLayout(new GridLayout(1, 3));
        
        JButton about = new JButton("About this game");
        about.addActionListener((e)->{
            getHostingPage().switchToSubpage(StartPage.ABOUT);
        });
        add(about);
        
        JButton play = new JButton("Play");
        play.addActionListener((e)->{
            getHostingPage().switchToSubpage(StartPage.PLAY);
        });
        add(play);
        
        JButton howToPlay = new JButton("How to play");
        howToPlay.addActionListener((e)->{
            getHostingPage().switchToSubpage(StartPage.HOW_TO_PLAY);
        });
        add(howToPlay);
    }
}
