package controllers;

import customizables.Build;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import customizables.AbstractCustomizable;
import util.CombatLog;
import windows.start.StartPage;

/**
 * MainWindow acts as the main frame for the game,
 * and serves as the controller for rendering the various
 * pages.
 * 
 * @author Matt Crow
 */
public class MainWindow extends JFrame{
    public MainWindow(){
        super();
        setTitle("The Orpheus Proposition");
        setContentPane(new StartPage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        setVisible(true);
		addWindowListener(winAdapt());
        AbstractCustomizable.loadAll();
        Build.loadAll();
    }
    
    private WindowAdapter winAdapt(){
        return new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                //CombatLog.displayLog();
                Master.SERVER.shutDown();
            }
        };
    }
    
    
    
    public static void main(String[] args){
        new MainWindow();
    }
}
