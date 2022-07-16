package orpheus.client.gui.pages.menu;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class Index extends Page{
    public Index(PageController host, ComponentFactory cf){
        super(host, cf);
        addMenuItem(cf.makeLabel("The Orpheus Proposition"));
        setLayout(new GridLayout(1, 3));
        
        JButton about = new JButton("About this game");
        about.addActionListener((e)->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("README.txt")));
        });
        add(about);
        
        JButton play = new JButton("Play");
        play.addActionListener((e)->{
            getHost().switchToPage(new StartPlay(host, cf));
        });
        add(play);
        
        JButton howToPlay = new JButton("How to play");
        howToPlay.addActionListener((e)->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("howToPlay.txt")));
        });
        add(howToPlay);
    }
    
    private String readFile(String fileName){
        StringBuilder bui = new StringBuilder();
        InputStream in = Index.class.getResourceAsStream("/" + fileName);
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
