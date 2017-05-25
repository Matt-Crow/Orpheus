package windows;

import javax.swing.JFrame;

public class StartWindow  extends JFrame{
	/**
	 * Startwindow is used as a 'launcher' for the game
	 */
	public static final long serialVersionUID = 1L;
	StartCanvas draw;
	
	public StartWindow(){
		setTitle("Orpheus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		
		draw = new StartCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
