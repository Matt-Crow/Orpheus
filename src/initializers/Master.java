package initializers;

import entities.TruePlayer;
import battle.Battle;

public class Master {
	public static final TruePlayer TRUEPLAYER = new TruePlayer();
	public static final int DETECTIONRANGE = 500;
	public static final int CANVASWIDTH = 600;
	public static final int CANVASHEIGHT = 600;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	public static final boolean ROTATECANVAS = true;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 16;
	
	private static Battle currentBattle;
	
	public static void setCurrentBattle(Battle b){
		currentBattle = b;
	}
	public static Battle getCurrentBattle(){
		return currentBattle;
	}
}
