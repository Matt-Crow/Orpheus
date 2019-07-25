package windows.start;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JLabel;
import windows.Page;

/**
 *
 * @author Matt
 */
public class StartMainMenu extends Page{
    public StartMainMenu(){
        addMenuItem(new JLabel("The Orpheus Proposition"));
        setLayout(new GridLayout(1, 3));
        
        JButton about = new JButton("About this game");
        about.addActionListener((e)->{
            getHost().switchToPage(new StartTextDisplay(readFile("README.txt")));
        });
        add(about);
        
        JButton play = new JButton("Play");
        play.addActionListener((e)->{
            getHost().switchToPage(new StartPlay());
        });
        add(play);
        
        JButton howToPlay = new JButton("How to play");
        howToPlay.addActionListener((e)->{
            getHost().switchToPage(new StartTextDisplay(readFile("howToPlay.txt")));
        });
        add(howToPlay);
    }
    
    private String readFile(String fileName){
        StringBuilder bui = new StringBuilder();
        InputStream in = StartMainMenu.class.getResourceAsStream("/" + fileName);
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
