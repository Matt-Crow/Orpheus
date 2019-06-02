package controllers;

import actives.LoadActives;
import customizables.LoadCharacterClasses;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import passives.LoadPassives;
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
        LoadActives.load();
        //AbstractActive.logAllPsuedoJson();
        //AbstractActive.readFile(Run.class.getClass().getResourceAsStream("/actives.csv"));
        LoadPassives.load();
		LoadCharacterClasses.load();
    }
    
    private WindowAdapter winAdapt(){
        return new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                //CombatLog.displayLog();
            }
        };
    }
    
    
    
    public static void main(String[] args){
        new MainWindow();
    }
}
