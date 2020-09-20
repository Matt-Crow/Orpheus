package util;

import world.customizables.DataSet;

public class Settings {
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	public static final boolean DISABLEPARTICLES = false; // causes lag
	public static final int FPS = 30;
    
    private static final DataSet DATA_SET = new DataSet();
    
    static {
        DATA_SET.loadDefaults();
    }
    
    public static DataSet getDataSet(){
        return DATA_SET;
    }
    
	// seconds to frames
	public static int seconds(int sec){
		return sec * FPS;
	}
	public static int framesToSeconds(int frames){
		return frames / FPS;
	}
}
