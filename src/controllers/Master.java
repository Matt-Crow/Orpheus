package controllers;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import entities.TruePlayer;
import battle.Battle;
import javax.swing.JFrame;

public class Master {
	public static final TruePlayer TRUEPLAYER = new TruePlayer();
	public static final int DETECTIONRANGE = 500;
	public static final int CANVASBOTTOM;// = Toolkit.getDefaultToolkit().getScreenInsets(new DrawingFrame().getGraphicsConfiguration()).bottom;
	public static final int CANVASWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int CANVASHEIGHT;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	public static final boolean DISABLEPARTICLES = false; // causes lag
	public static final int FPS = 30;
	public static final int UNITSIZE = 100;
	
	private static Battle currentBattle;
    
    static {
        JFrame f = new JFrame();
        CANVASBOTTOM = Toolkit.getDefaultToolkit().getScreenInsets(f.getGraphicsConfiguration()).bottom;
        CANVASHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - CANVASBOTTOM;
        f.dispose();
    }
	
	public static void setCurrentBattle(Battle b){
		currentBattle = b;
	}
    public static Battle getCurrentBattle(){
        return currentBattle;
    }
    
	// seconds to frames
	public static int seconds(int sec){
		return sec * FPS;
	}
	public static int framesToSeconds(int frames){
		return frames / FPS;
	}
	
	public static int getMouseX(){
		// returns the position of the mouse cursor relative to the battlefield corner
		Point p = MouseInfo.getPointerInfo().getLocation();
		int[] t = currentBattle.getCanvas().getLastTransform();
		return (int)p.getX() - t[0];
	}
	public static int getMouseY(){
		// returns the position of the mouse cursor relative to the battlefield corner
		Point p = MouseInfo.getPointerInfo().getLocation();
		int[] t = currentBattle.getCanvas().getLastTransform();
		return (int)p.getY() - t[1];
	}
}
