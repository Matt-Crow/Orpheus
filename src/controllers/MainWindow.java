package controllers;

import customizables.Build;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import upgradables.AbstractUpgradable;
import util.CombatLog;
import windows.StartCanvas;

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
        setContentPane(new StartCanvas());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        setVisible(true);
		addWindowListener(winAdapt());
        AbstractUpgradable.loadAll();
        Build.loadAll();
    }
    
    private WindowAdapter winAdapt(){
        return new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                //CombatLog.displayLog();
                if(Master.getServer() != null){
                    Master.getServer().shutDown();
                }
            }
        };
    }
    
    
    
    public static void main(String[] args){
        new MainWindow();
    }
}
