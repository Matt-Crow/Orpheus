package controllers;

import java.awt.Toolkit;
import entities.TruePlayer;
import java.io.IOException;
import javax.swing.JFrame;
import net.OrpheusServer;

public class Master {
	public static final TruePlayer TRUEPLAYER = new TruePlayer();
	public static final int DETECTIONRANGE = 500;
	public static final int CANVASBOTTOM;
    public static final int CANVASWIDTH;
	public static final int CANVASHEIGHT;
	public static final boolean DISABLEHEALING = false;
	public static final boolean DISABLEALLAI = false;
	// number of angles a player can have
	public static final int TICKSTOROTATE = 36;
	public static final boolean DISABLEPARTICLES = false; // causes lag
	public static final int FPS = 30;
	public static final int UNITSIZE = 100;
    private static OrpheusServer server = null;
    
    static {
        JFrame f = new JFrame();
        CANVASBOTTOM = Toolkit.getDefaultToolkit().getScreenInsets(f.getGraphicsConfiguration()).bottom;
        CANVASHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - CANVASBOTTOM;
        CANVASWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        f.dispose();
    }
    
	// seconds to frames
	public static int seconds(int sec){
		return sec * FPS;
	}
	public static int framesToSeconds(int frames){
		return frames / FPS;
	}
    
    public static void startServer() throws IOException{
        if(server == null){
            server = new OrpheusServer(5000);
        }
    }
    public static OrpheusServer getServer(){
        return server;
    }
}
