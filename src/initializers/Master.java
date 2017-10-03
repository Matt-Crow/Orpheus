package initializers;

//import java.awt.Toolkit;
import entities.TruePlayer;
import battle.Battle;

public class Master {
	public static final TruePlayer TRUEPLAYER = new TruePlayer();
	public static final int DETECTIONRANGE = 500;
	// need to figure out toolbar strip at the bottom
	//public static final int CANVASWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	//public static final int CANVASHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	public static final int CANVASWIDTH = 600;
	public static final int CANVASHEIGHT = 600;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	public static final boolean ROTATECANVAS = true;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	
	private static Battle currentBattle;
	
	public static void setCurrentBattle(Battle b){
		currentBattle = b;
	}
	public static Battle getCurrentBattle(){
		return currentBattle;
	}
}
