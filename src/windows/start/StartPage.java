package windows.start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import windows.Page;

/**
 *
 * @author Matt Crow
 */
public class StartPage extends Page{
    public static final String MAIN = "MAIN";
    public static final String ABOUT = "ABOUT";
    public static final String HOW_TO_PLAY = "HOW TO PLAY";
    
    public StartPage(){
        super();
        JButton title = new JButton("The Orpheus Proposition");
        title.addActionListener((e)->{
            switchToSubpage(MAIN);
        });
        title.setToolTipText("click here to go to the main menu");
        addMenuItem(title);
        
        String aboutText = "";
        InputStream in = StartPage.class.getResourceAsStream("/README.txt");
        if(in != null){
            BufferedReader buff = new BufferedReader(new InputStreamReader(in));
            try {
                while(buff.ready()){
                    aboutText += (buff.readLine() + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        addSubPage(MAIN, new StartMainMenu(this));
        addSubPage(ABOUT, new StartTextDisplay(this, readFile("README.txt")));
        addSubPage(HOW_TO_PLAY, new StartTextDisplay(this, readFile("howToPlay.txt")));
    }
    
    private String readFile(String fileName){
        StringBuilder bui = new StringBuilder();
        InputStream in = StartPage.class.getResourceAsStream("/" + fileName);
        if(in != null){
            BufferedReader buff = new BufferedReader(new InputStreamReader(in));
            try {
                while(buff.ready()){
                    bui.append(buff.readLine()).append('\n');
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bui.toString();
    }
}
