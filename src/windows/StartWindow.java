package windows;

import javax.swing.JFrame;

import initializers.Master;

public class StartWindow  extends JFrame{
	/**
	 * Startwindow is used as a 'launcher' for the game
	 */
	public static final long serialVersionUID = 1L;
	StartCanvas draw;
	
	public StartWindow(){
		setTitle("Orpheus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		
		draw = new StartCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
