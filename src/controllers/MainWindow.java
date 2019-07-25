package controllers;

import customizables.Build;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import customizables.AbstractCustomizable;
import java.awt.GridLayout;
import javax.swing.JPanel;
import util.CombatLog;
import windows.Page;
import windows.PageSwitchListener;
import windows.start.StartMainMenu;

/**
 * MainWindow acts as the main frame for the game,
 * and serves as the controller for rendering the various
 * pages.
 * 
 * @author Matt Crow
 */
public class MainWindow extends JFrame{
    private final JPanel content;
    private Page currentPage;
    
    private static MainWindow instance = null;
    
    private MainWindow(){
        super();
        
        if(instance != null){
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
        
        currentPage = null;
        
        switchToPage(new StartMainMenu());
        
        AbstractCustomizable.loadAll();
        Build.loadAll();
        
        instance = this;
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
        if(currentPage != null && currentPage instanceof PageSwitchListener){
            ((PageSwitchListener)currentPage).leavingPage(currentPage);
        }
        currentPage = p;
        if(p instanceof PageSwitchListener){
            ((PageSwitchListener)currentPage).switchedToPage(p);
        }
        return this;
    }
    
    public static MainWindow getInstance(){
        if(instance == null){
            instance = new MainWindow();
        }
        return instance;
    }
    
    public static void main(String[] args){
        new MainWindow();
    }
}
