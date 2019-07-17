package windows.start;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import windows.MainCanvas;
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
            JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
            parent.setContentPane(new MainCanvas());
            parent.revalidate();
            p.requestFocus(); 
        });
        add(play);
        
        JButton howToPlay = new JButton("How to play");
        add(howToPlay);
    }
}
