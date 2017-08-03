package initializers;

import battle.Battle;

public class Master {
	public static final int CANVASWIDTH = 600;
	public static final int CANVASHEIGHT = 600;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	
	private static Battle currentBattle;
	
	public static void setCurrentBattle(Battle b){
		currentBattle = b;
	}
	public static Battle getCurrentBattle(){
		return currentBattle;
	}
}
