package controllers;

import customizables.Build;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import customizables.AbstractCustomizable;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JPanel;
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
    //might not want to do this: uses a lot of memory to keep holding Pages
    
    private final JPanel content;
    private Page currentPage;
    
    private static boolean hasInstance = false;
    
    public MainWindow(){
        super();
        
        if(hasInstance){
            throw new ExceptionInInitializerError("Can only have 1 instance of MainWindow");
        }
        
        setTitle("The Orpheus Proposition");
        
        content = new JPanel();
        content.setLayout(new GridLayout(1, 1));
        setContentPane(content);
        
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
        
        switchToPage(new StartPage(this));
        
        AbstractCustomizable.loadAll();
        Build.loadAll();
        
        hasInstance = true;
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
    
    public MainWindow switchToPage(Page p){
        if(p == null){
            throw new NullPointerException();
        }
        content.removeAll();
        content.add(p);
        content.revalidate();
        p.requestFocus();
        content.repaint();
        if(currentPage != null){
            currentPage.switchedFromThis();
        }
        currentPage = p;
        p.switchedToThis();
        return this;
    }
    
    
    
    public static void main(String[] args){
        new MainWindow();
    }
}
