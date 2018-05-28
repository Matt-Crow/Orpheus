package windows;

public class StartWindow  extends DrawingFrame{
	/**
	 * Startwindow is used as a 'launcher' for the game
	 */
	public static final long serialVersionUID = 1L;
	StartCanvas draw;
	
	public StartWindow(){
		super();
		setTitle("Orpheus");
		
		draw = new StartCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
