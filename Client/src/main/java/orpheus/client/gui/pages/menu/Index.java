package orpheus.client.gui.pages.menu;

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
public class Index extends Page{
    public Index(PageController host, ComponentFactory cf){
        super(host, cf);
        addMenuItem(cf.makeSpaceAround(cf.makeLabel("The Orpheus Proposition"), Color.YELLOW));
        setLayout(new GridLayout(1, 3));
        
        // not sure how I feel about small buttons
        add(cf.makeSpaceAround(cf.makeButton("About this game", ()->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("README.txt")));
        }), Color.RED));
        
        add(cf.makeSpaceAround(cf.makeButton("Play", ()->{
            getHost().switchToPage(new StartPlay(host, cf));
        }), Color.GREEN));
        
        add(cf.makeSpaceAround(cf.makeButton("How to play", ()->{
            getHost().switchToPage(new StartTextDisplay(host, cf, readFile("howToPlay.txt")));
        }), Color.BLUE));
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
