package controllers;

import customizables.Build;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import customizables.AbstractCustomizable;
import java.util.HashMap;
import util.CombatLog;
import windows.Page;
import windows.start.StartPage;

/**
 * MainWindow acts as the main frame for the game,
 * and serves as the controller for rendering the various
 * pages.
 * 
 * @author Matt Crow
 */
public class MainWindow extends JFrame{
    private final HashMap<String, Page> pages;
    private Page currentPage;
    
    public MainWindow(){
        super();
        setTitle("The Orpheus Proposition");
        setContentPane(new StartPage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        setVisible(true);
		addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                //CombatLog.displayLog();
                Master.SERVER.shutDown();
            }
        });
        
        pages = new HashMap<>();
        currentPage = null;
        
        AbstractCustomizable.loadAll();
        Build.loadAll();
    }
    
    /**
     * Adds a page to this controller.
     * 
     * @param pageName the name associated with the new page.
     * calling switchToPage(pageName) will switch this to rendering
     * the given page. If pageName is already used by this, will throw
     * an exception.
     * 
     * @param p the page to add to this
     * 
     * @return this
     */
    public MainWindow addPage(String pageName, Page p){
        if(pages.containsKey(pageName)){
            throw new IllegalArgumentException("There is alread a page with the name \'" + pageName + "\'");
        }
        pages.put(pageName, p);
        return this;
    }
    
    public MainWindow switchToPage(String pageName){
        if(pages.containsKey(pageName)){
            
            setContentPane(pages.get(pageName));
        }
        return this;
    }
    
    
    
    public static void main(String[] args){
        new MainWindow();
    }
}
