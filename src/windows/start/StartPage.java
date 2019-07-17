package windows.start;

import javax.swing.JButton;
import windows.Page;

/**
 *
 * @author Matt Crow
 */
public class StartPage extends Page{
    public static final String MAIN = "MAIN";
    
    public StartPage(){
        super();
        JButton title = new JButton("The Orpheus Proposition");
        title.addActionListener((e)->{
            switchToSubpage(MAIN);
        });
        title.setToolTipText("click here to go to the main menu");
        addMenuItem(title);
        
        addSubPage(MAIN, new StartMainMenu(this));
    }
}
