package initializers;

import java.awt.Toolkit;
import entities.TruePlayer;
import resources.DrawingFrame;
import battle.Battle;

public class Master {
	public static final TruePlayer TRUEPLAYER = new TruePlayer();
	public static final int DETECTIONRANGE = 500;
	
	public static final int CANVASBOTTOM = Toolkit.getDefaultToolkit().getScreenInsets(new DrawingFrame().getGraphicsConfiguration()).bottom;
	public static final int CANVASWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int CANVASHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - CANVASBOTTOM;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = !false;
	public static final boolean ROTATECANVAS = !true;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	public static final boolean DISABLEPARTICLES = !false; // causes too much lag
	public static final int FPS = 60;
	
	private static Battle currentBattle;
	
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
}
