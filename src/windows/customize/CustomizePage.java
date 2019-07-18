package windows.customize;

import javax.swing.JButton;
import windows.Page;
import windows.start.StartPage;

/**
 *
 * @author Matt
 */
public class CustomizePage extends Page{
    public static final String MAIN = "MAIN";
    
    public CustomizePage(){
        super();
        JButton exit = new JButton("Quit");
        exit.addActionListener((e)->{
            StartPage p = new StartPage();
            switchToPage(p);
            p.switchToSubpage(StartPage.PLAY);
        });
        addMenuItem(exit);
        addSubPage(MAIN, new CustomizeMain(this));
    }
}
