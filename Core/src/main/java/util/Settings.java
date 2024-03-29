package util;

public class Settings {
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	/**
	 * number of angles a player can have - also affects particle spawn rate
	 */
	public static final int TICKSTOROTATE = 16;
	public static final boolean DISABLEPARTICLES = false;
	public static final int FPS = 30;
    
	// seconds to frames
	public static int seconds(double sec){
		return (int) (sec * FPS);
	}
	public static double framesToSeconds(int frames){
		return ((double)frames) / FPS;
	}
}
