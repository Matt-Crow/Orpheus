package controllers;

import customizables.DataSet;
import javax.swing.JOptionPane;
import net.OrpheusServer;

public class Master {
	public static final int DETECTIONRANGE = 500;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	public static final boolean DISABLEPARTICLES = false; // causes lag
	public static final int FPS = 30;
	public static final int UNITSIZE = 100;
    public static final OrpheusServer SERVER = new OrpheusServer();
    
    private static User user;
    
    private static final DataSet DATA_SET = new DataSet();
    
    static {
        user = new User();
        DATA_SET.loadDefaults();
    }
    
    
    public static void loginWindow(){
        if(user.getName().equals(User.DEFAULT_NAME)){
            user.setName(JOptionPane.showInputDialog("Enter a name: "));
        }
    }
    //todo: login credentials
    
    public static User getUser(){
        if(user == null){
            throw new NullPointerException("no user is logged in");
        }
        return user;
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
