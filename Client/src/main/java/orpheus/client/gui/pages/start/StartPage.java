package orpheus.client.gui.pages.start;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class StartPage extends Page{
    public StartPage(PageController host, ComponentFactory cf){
        super(host, cf);
        addMenuItem(cf.makeSpaceAround(cf.makeLabel("The Orpheus Proposition")));
        setLayout(new GridLayout(1, 3));
        
        add(cf.makeButton("About this game", ()->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("README.txt")));
        }));
        
        add(cf.makeButton("Play", ()->{
            getHost().switchToPage(new StartPlay(host, cf));
        }));
        
        add(cf.makeButton("How to play", ()->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("howToPlay.txt")));
        }));
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
